package labyrinth.solver;

import labyrinth.LabyrinthBase;
import labyrinth.LabyrinthFunctionLibrary;

import java.util.*;

public class HexagonLabyrinthSolver implements ILabyrinthSolver {

    private List<int[]> path;

    public List<int[]> solveWithDijkstraAlgorithm(LabyrinthBase labyrinth) {
        // Initialisation
        int rows = labyrinth.getVerticalWalls().length;
        int cols = labyrinth.getVerticalWalls()[0].length;
        int[][] distances = new int[cols][rows];
        int[][] prev = new int[cols][rows];
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> distances[a[0]][a[1]]));

        for (int[] row : distances) Arrays.fill(row, Integer.MAX_VALUE); // INFINITY
        for (int[] row : prev) Arrays.fill(row, -1); // Undefined

        // Source distance
        distances[0][labyrinth.getStart()] = 0;
        queue.offer(new int[]{0, labyrinth.getStart()});

        // Tant que la file d'attente n'est pas vide
        while (!queue.isEmpty()) {
            int[] u = queue.poll();
            int x = u[0], y = u[1];

            // Si c'est la sortie
            if (x == cols - 1 && y == labyrinth.getEnd()) break;

            // Vérification des voisins (haut, bas, gauche, droite)
            for (int[] direction : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
                int newX = x + direction[0], newY = y + direction[1];
                if (newX >= 0 && newX < cols && newY >= 0 && newY < rows && labyrinth.isMovePossible(x, y, newX, newY)) {
                    int newDist = distances[x][y] + 1; // Coût de déplacement uniforme
                    if (newDist < distances[newX][newY]) {
                        distances[newX][newY] = newDist;
                        prev[newX][newY] = LabyrinthFunctionLibrary.encodePosition(x, y, labyrinth.getVerticalWalls()); // Encode current position as previous
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
        }

        // Reconstruire le chemin
        path = new ArrayList<>();
        int[] tracePos = {cols - 1, labyrinth.getEnd()};;
        while (tracePos != null) {
            path.add(0, tracePos); // Ajoute au début pour reconstruire le chemin
            int encodedPos = prev[tracePos[0]][tracePos[1]];
            tracePos = LabyrinthFunctionLibrary.decodePosition(encodedPos, labyrinth.getVerticalWalls());
        }

        return path;
    }

    public List<int[]> solveWithRightHandAlgorithm(LabyrinthBase labyrinth) {
        path = new ArrayList<>(); // Pour stocker le chemin emprunté

        // Commencer à l'entrée du labyrinthe
        int x = 0, y = labyrinth.getStart();
        int direction = 0; // Représenter les directions avec des chiffres : 0 = Est, 1 = Sud, 2 = Ouest, 3 = Nord

        path.add(new int[] {x, y}); // Ajouter la position de départ au chemin

        while (true) {
            // Vérifier si nous sommes à la sortie
            if (x == labyrinth.getVerticalWalls()[0].length - 1 && y == labyrinth.getEnd()) {
                break;
            }

            boolean moved = false;

            // Essayez de tourner à droite et de bouger dans cette direction
            int newDirection = (direction + 3) % 4;
            int[] nextMove = labyrinth.getNextMove(x, y, newDirection);

            if (labyrinth.isMovePossible(x, y, nextMove[0], nextMove[1])) {
                direction = newDirection;
                x = nextMove[0];
                y = nextMove[1];
                path.add(new int[]{x, y});
                moved = true;
            } else {
                // Essayez de bouger tout droit si bouger à droite n'est pas possible
                nextMove = labyrinth.getNextMove(x, y, direction);
                if (labyrinth.isMovePossible(x, y, nextMove[0], nextMove[1])) {
                    x = nextMove[0];
                    y = nextMove[1];
                    path.add(new int[]{x, y});
                    moved = true;
                }
            }

            if (!moved) {
                // Si nous ne pouvons pas bouger à droite ou tout droit, alors nous tournons à gauche
                direction = (direction + 1) % 4;
            }
        }
        return path;
    }

    public List<int[]> getPath() {
        return path;
    }

}
