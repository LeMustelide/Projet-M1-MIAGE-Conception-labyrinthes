package labyrinth;

import java.util.Random;

public class Labyrinth {
    public boolean[][] verticalWalls;    // Murs verticaux: (m+1) x n
    public boolean[][] horizontalWalls;  // Murs horizontaux: m x (n+1)
    public UnionFind uf;

    public int start, end;

    public Labyrinth(int m, int n) {
        this.verticalWalls = new boolean[m][n+1];
        this.horizontalWalls = new boolean[m+1][n];
        this.uf = new UnionFind(m * n);

        // Initialiser tous les murs à 'fermé'
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n+1; j++) {
                verticalWalls[i][j] = true;
            }
        }

        for(int i = 0; i < m+1; i++) {
            for(int j = 0; j < n; j++) {
                horizontalWalls[i][j] = true;
            }
        }
    }

    public void generatePerfectLabyrinth() {
        Random rand = new Random();
        int line = verticalWalls.length;
        int col = horizontalWalls[0].length;

        int wallsToOpen = line * col - 1;

        while (wallsToOpen > 0) {
            int y = rand.nextInt(line);
            int x = rand.nextInt(col);

            // Choisir la direction du mur : horizontal ou vertical
            boolean chooseHorizontal = rand.nextBoolean();

            if (chooseHorizontal && y < line - 1) {
                // Vérifier les cellules en haut et en bas du mur horizontal
                int cell1Id = (y * col + x);
                int cell2Id = ((y + 1) * col + x);
                if (uf.find(cell1Id) != uf.find(cell2Id) && uf.find(cell2Id) != -1 && verticalWalls[y+1][x]) {
                    horizontalWalls[y+1][x] = false;
                    uf.union(cell1Id, cell2Id);
                    wallsToOpen--;
                }
            } else if (!chooseHorizontal && x < col - 1) {
                // Vérifier les cellules à gauche et à droite du mur vertical
                int cell1Id = (y * col + x);
                int cell2Id = (y * col + (x + 1));
                if (uf.find(cell1Id) != uf.find(cell2Id) && uf.find(cell2Id) != -1 && verticalWalls[y][x+1]) {
                    verticalWalls[y][x+1] = false;
                    uf.union(cell1Id, cell2Id);
                    wallsToOpen--;
                }
            }
        }

        start = rand.nextInt(col);
        end = rand.nextInt(col);


        verticalWalls[start][0] = false;
        verticalWalls[end][line] = false;
    }
}
