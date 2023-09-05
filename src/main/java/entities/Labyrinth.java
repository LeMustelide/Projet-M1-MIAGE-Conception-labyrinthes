package entities;

import java.util.Random;

public class Labyrinth {
    public boolean[][] verticalWalls;    // Murs verticaux: (m+1) x n
    public boolean[][] horizontalWalls;  // Murs horizontaux: m x (n+1)
    UnionFind uf;

    public Labyrinth(int m, int n) {
        this.verticalWalls = new boolean[m + 1][n];
        this.horizontalWalls = new boolean[m][n + 1];
        this.uf = new UnionFind(m * n);

        // Initialiser tous les murs à 'fermé'
        for(int i = 0; i <= m; i++) {
            for(int j = 0; j < n; j++) {
                verticalWalls[i][j] = true;
            }
        }

        for(int i = 0; i < m; i++) {
            for(int j = 0; j <= n; j++) {
                horizontalWalls[i][j] = true;
            }
        }
    }

    public void checkAndOpenWall() {
        Random rand = new Random();

        while (true) {
            int i = rand.nextInt(verticalWalls.length - 1);
            int j = rand.nextInt(verticalWalls[0].length);

            boolean chooseHorizontal = rand.nextBoolean();

            if (chooseHorizontal && i < horizontalWalls.length) {
                if (uf.find(i * horizontalWalls[0].length + j) != uf.find((i + 1) * horizontalWalls[0].length + j)) {
                    horizontalWalls[i][j] = false;
                    uf.union(i * horizontalWalls[0].length + j, (i + 1) * horizontalWalls[0].length + j);
                    break;
                }
            } else if (!chooseHorizontal && j < verticalWalls[0].length - 1) {
                if (uf.find(i * verticalWalls[0].length + j) != uf.find(i * verticalWalls[0].length + (j + 1))) {
                    verticalWalls[i][j] = false;
                    uf.union(i * verticalWalls[0].length + j, i * verticalWalls[0].length + (j + 1));
                    break;
                }
            }
        }
    }

    public void generatePerfectLabyrinth() {
        Random rand = new Random();
        int m = verticalWalls.length - 1;
        int n = verticalWalls[0].length;

        int wallsToOpen = m * n - 1;

        while (wallsToOpen > 0) {
            int i = rand.nextInt(m);
            int j = rand.nextInt(n);

            // Choisir la direction du mur : horizontal ou vertical
            boolean chooseHorizontal = rand.nextBoolean();

            if (chooseHorizontal && i < m - 1) {
                // Vérifier les cellules en haut et en bas du mur horizontal
                int cell1Id = i * n + j;
                int cell2Id = (i + 1) * n + j;
                if (uf.find(cell1Id) != uf.find(cell2Id)) {
                    horizontalWalls[i][j] = false;
                    uf.union(cell1Id, cell2Id);
                    wallsToOpen--;
                }
            } else if (!chooseHorizontal && j < n - 1) {
                // Vérifier les cellules à gauche et à droite du mur vertical
                int cell1Id = i * n + j;
                int cell2Id = i * n + (j + 1);
                if (uf.find(cell1Id) != uf.find(cell2Id)) {
                    verticalWalls[i][j] = false;
                    uf.union(cell1Id, cell2Id);
                    wallsToOpen--;
                }
            }
        }
    }

}
