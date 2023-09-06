package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

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

    public void generatePerfectLabyrinth() {
        Random rand = new Random();
        int col = verticalWalls.length ;
        int line = horizontalWalls.length ;

        System.out.println("col : " + col);
        System.out.println("line : " + line);

        System.out.println("col : " + col);
        System.out.println("line : " + line);

        int wallsToOpen = col * line - 1 - 5;

        while (wallsToOpen > 0) {
            int x = rand.nextInt(0, col);
            int y = rand.nextInt(0, line);

            // Choisir la direction du mur : horizontal ou vertical
            boolean chooseHorizontal = rand.nextBoolean();
            if (chooseHorizontal && x < col - 1 && x < line - 1) {
                // Vérifier les cellules en haut et en bas du mur horizontal
                int cell1Id = x * line + y;
                int cell2Id = (x + 1) * line + y;
                int group1 = uf.find(cell1Id);
                int group2 = uf.find(cell2Id);
                if (group1 != group2) {
                    horizontalWalls[2][y] = false;
                    System.out.println("delete wall : " + x + " " + y);
                    System.out.println(cell1Id);
                    uf.union(cell1Id, cell2Id);
                    wallsToOpen--;
                }
            } else if (!chooseHorizontal && y < line - 1) {
                // Vérifier les cellules à gauche et à droite du mur vertical
                int cell1Id = x * line + y;
                int cell2Id = x * line + (y + 1);
                int group1 = uf.find(cell1Id);
                int group2 = uf.find(cell2Id);
                if (group1 != group2) {
                    //verticalWalls[x][y] = false;
                    uf.union(cell1Id, cell2Id);
                    wallsToOpen--;
                }
            }

            //System.out.println(wallsToOpen);

            /*
            if(wallsToOpen == 8) {
                System.out.println("--- Liste de groupes ---");
                List<Integer> groups = new ArrayList<>();
                for (int h = 0; h < col*line - 1; h++) {
                    if(!groups.contains(uf.find(h))) {
                        System.out.println("groupe : " + uf.find(h));
                        groups.add(uf.find(h));
                    }
                }
                System.out.println("--- Fin de la liste ---");
            }

             */

            /*
            if(wallsToOpen > 0 && wallsToOpen < 2){
                int start = rand.nextInt(0, col);
                int end = rand.nextInt(0, col);

                verticalWalls[start][0] = false;
                verticalWalls[end][line-1] = false;
                wallsToOpen = 0;
            }
            */
        }
    }

}
