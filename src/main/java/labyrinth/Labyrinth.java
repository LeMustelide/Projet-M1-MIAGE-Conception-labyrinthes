package labyrinth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Labyrinth {

    private Random rand = new Random();
    public boolean[][] verticalWalls;    // Murs verticaux: (m+1) x n
    public boolean[][] horizontalWalls;  // Murs horizontaux: m x (n+1)
    UnionFind uf;

    public int start, end;

    public Labyrinth(int m, int n) {
        this.verticalWalls = new boolean[m][n+1];
        this.horizontalWalls = new boolean[m+1][n];
        this.uf = new UnionFind(m * n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n+1; j++) {
                verticalWalls[i][j] = true;
            }
        }

        for (int i = 0; i < m+1; i++) {
            for (int j = 0; j < n; j++) {
                horizontalWalls[i][j] = true;
            }
        }
    }

    public void generatePerfectLabyrinth() {
        Random rand = new Random();
        int line = verticalWalls.length;
        int col = horizontalWalls[0].length;

        ArrayList<Integer> walls = new ArrayList<>();

        for (int y = 0; y < line; y++) {
            for (int x = 0; x < col; x++) {
                if (y < line - 1) {
                    walls.add(encodeWall(x, y, true));
                }
                if (x < col - 1) {
                    walls.add(encodeWall(x, y, false));
                }
            }
        }

        shuffle(walls);

        for (int wall : walls) {
            int x, y;
            boolean isHorizontal;
            x = decodeX(wall);
            y = decodeY(wall);
            isHorizontal = decodeOrientation(wall);

            int cell1Id, cell2Id;
            if (isHorizontal) {
                cell1Id = (y * col + x);
                cell2Id = ((y + 1) * col + x);
                int cell1Parent = uf.find(cell1Id);
                int cell2Parent = uf.find(cell2Id);
                if (cell1Parent != cell2Parent && cell2Parent != -1 && horizontalWalls[y+1][x]) {
                    horizontalWalls[y+1][x] = false;
                    uf.union(cell1Id, cell2Id);
                }
            } else {
                cell1Id = (y * col + x);
                cell2Id = (y * col + (x + 1));
                int cell1Parent = uf.find(cell1Id);
                int cell2Parent = uf.find(cell2Id);
                if (cell1Parent != cell2Parent && cell2Parent != -1 && verticalWalls[y][x+1]) {
                    verticalWalls[y][x+1] = false;
                    uf.union(cell1Id, cell2Id);
                }
            }

            if (uf.numSets == 1) {
                break;
            }
        }

        start = rand.nextInt(col);
        end = rand.nextInt(col);

        verticalWalls[start][0] = false;
        verticalWalls[end][line] = false;
    }


    private int encodeWall(int x, int y, boolean isHorizontal) {
        return (x << 1) | (y << 16) | (isHorizontal ? 1 : 0);
    }

    private int decodeX(int wall) {
        return (wall >> 1) & 0x7FFF;
    }

    private int decodeY(int wall) {
        return (wall >> 16) & 0x7FFF;
    }

    private boolean decodeOrientation(int wall) {
        return (wall & 1) == 1;
    }

    private void shuffle(List<?> list) {
        int size = list.size();
        for (int i = size - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Collections.swap(list, i, index);
        }
    }

}
