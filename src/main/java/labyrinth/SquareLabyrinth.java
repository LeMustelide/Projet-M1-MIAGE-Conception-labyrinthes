package labyrinth;

import labyrinth.view.Shape;

import java.util.*;

public class SquareLabyrinth extends LabyrinthBase {

    private Shape shape;

    public SquareLabyrinth(int m, int n, int edges) {
        super(m, n);
        super.addVerticalWalls(m, n);
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

    public List<int[]> solveWithRightHandAlgorithm() {
        path = new ArrayList<>(); // Pour stocker le chemin emprunté

        // Commencer à l'entrée du labyrinthe
        int x = 0, y = start;
        int direction = 0; // Représenter les directions avec des chiffres : 0 = Est, 1 = Sud, 2 = Ouest, 3 = Nord

        path.add(new int[] {x, y}); // Ajouter la position de départ au chemin

        while (true) {
            // Vérifier si nous sommes à la sortie
            if (x == horizontalWalls[0].length - 1 && y == end) {
                break;
            }

            boolean moved = false;

            // Essayez de tourner à droite et de bouger dans cette direction
            int newDirection = (direction + 3) % 4;
            int[] nextMove = getNextMove(x, y, newDirection);

            if (isMovePossible(x, y, nextMove[0], nextMove[1])) {
                direction = newDirection;
                x = nextMove[0];
                y = nextMove[1];
                path.add(new int[]{x, y});
                moved = true;
            } else {
                // Essayez de bouger tout droit si bouger à droite n'est pas possible
                nextMove = getNextMove(x, y, direction);
                if (isMovePossible(x, y, nextMove[0], nextMove[1])) {
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

    private boolean isMovePossible(int x, int y, int newX, int newY) {
        // Vérifier les limites du labyrinthe
        if (newX < 0 || newY < 0 || newX >= verticalWalls[0].length || newY >= horizontalWalls.length) {
            return false; // Le mouvement est en dehors des limites du labyrinthe
        }

        // Déplacement vers l'est
        if (newX > x) {
            return !verticalWalls[y][x + 1]; // Il ne doit pas y avoir de mur à droite
        }

        // Déplacement vers l'ouest
        if (newX < x) {
            return !verticalWalls[y][x]; // Il ne doit pas y avoir de mur à gauche
        }

        // Déplacement vers le sud
        if (newY > y) {
            return !horizontalWalls[y + 1][x]; // Il ne doit pas y avoir de mur en bas
        }

        // Déplacement vers le nord
        if (newY < y) {
            return !horizontalWalls[y][x]; // Il ne doit pas y avoir de mur en haut
        }

        return false; // Par défaut, le mouvement n'est pas possible
    }

    private int[] getNextMove(int x, int y, int direction) {
        switch (direction) {
            case 0: return new int[]{x + 1, y}; // Est
            case 1: return new int[]{x, y + 1}; // Sud
            case 2: return new int[]{x - 1, y}; // Ouest
            case 3: return new int[]{x, y - 1}; // Nord
            default: throw new IllegalArgumentException("Direction non valide");
        }
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public int getEntry() {
        return start;
    }

    public int getExit() {
        return end;
    }

    public List<int[]> getPath() {
        return path;
    }

}
