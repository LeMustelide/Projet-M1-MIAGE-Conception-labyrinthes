package labyrinth.solver;

import labyrinth.HexagonLabyrinth;
import labyrinth.LabyrinthBase;

import java.util.*;

public class HexagonLabyrinthSolver implements ILabyrinthSolver {

    private List<int[]> path;

    // Directions pour un labyrinthe hexagonal
    private static final int[][] HEX_DIRECTIONS = {
            {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {0, 1}, {1, 1}
    };

    @Override
    public List<int[]> solveWithDijkstraAlgorithm(LabyrinthBase labyrinth) {
        int rows = labyrinth.getM();
        int cols = labyrinth.getN();
        int[][] distances = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> distances[a[0]][a[1]]));

        for (int[] row : distances) Arrays.fill(row, Integer.MAX_VALUE);

        distances[labyrinth.getStart()][0] = 0;
        queue.offer(new int[]{labyrinth.getEnd(), 0});

        while (!queue.isEmpty()) {
            int[] u = queue.poll();
            int x = u[0], y = u[1];

            if (visited[x][y]) continue;
            visited[x][y] = true;

            if (x == rows - 1 && y == labyrinth.getEnd()) break;

            for (int[] direction : HEX_DIRECTIONS) {
                int newX = x + direction[0], newY = y + direction[1];
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && !visited[newX][newY] && labyrinth.isMovePossible(x, y, newX, newY)) {
                    int newDist = distances[x][y] + 1;
                    if (newDist < distances[newX][newY]) {
                        distances[newX][newY] = newDist;
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
        }

        path = reconstructPath(distances, labyrinth.getStart(), labyrinth.getEnd());
        return path;
    }

    private List<int[]> reconstructPath(int[][] distances, int startRow, int endRow) {
        List<int[]> path = new ArrayList<>();
        int x = distances.length - 1, y = endRow;
        path.add(new int[]{x, y});

        while (x != startRow || y != 0) {
            for (int[] direction : HEX_DIRECTIONS) {
                int newX = x - direction[0], newY = y - direction[1];
                if (newX >= 0 && newX < distances.length && newY >= 0 && newY < distances[0].length && distances[newX][newY] == distances[x][y] - 1) {
                    path.add(0, new int[]{newX, newY});
                    x = newX;
                    y = newY;
                    break;
                }
            }
        }

        return path;
    }

    @Override
    public List<int[]> solveWithRightHandAlgorithm(LabyrinthBase labyrinth) {
        path = new ArrayList<>();

        int x = labyrinth.getStart(), y = 0;
        int direction = 0;

        path.add(new int[]{x, y});

        while (x != labyrinth.getM() - 1 || y != labyrinth.getEnd()) {
            System.out.println("te");
            boolean moved = false;

            for (int i = 0; i < HEX_DIRECTIONS.length; i++) {
                int newDirection = (direction + i) % HEX_DIRECTIONS.length;
                int[] nextMove = getNextMove(x, y, newDirection, labyrinth);

                if (nextMove != null && labyrinth.isMovePossible(x, y, nextMove[0], nextMove[1])) {
                    x = nextMove[0];
                    y = nextMove[1];
                    path.add(new int[]{x, y});
                    direction = newDirection;
                    moved = true;
                    break;
                }
            }

            if (!moved) direction = (direction + 1) % HEX_DIRECTIONS.length;
        }

        return path;
    }

    private int[] getNextMove(int x, int y, int direction, LabyrinthBase labyrinth) {
        int newX = x + HEX_DIRECTIONS[direction][0];
        int newY = y + HEX_DIRECTIONS[direction][1];
        if (newX >= 0 && newX < labyrinth.getM() && newY >= 0 && newY < labyrinth.getN()) {
            return new int[]{newX, newY};
        }
        return null;
    }

    @Override
    public List<int[]> getPath() {
        System.out.println(path);
        return path;
    }
}
