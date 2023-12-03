package labyrinth;

import labyrinth.generator.HexagonLabyrinthGenerator;
import labyrinth.labyrinthCanvasGenerator.HexagonLabyrinthCanvasGenerator;
import labyrinth.solver.HexagonLabyrinthSolver;
import labyrinth.view.Shape;

public class HexagonLabyrinth extends LabyrinthBase {

    private Shape shape;

    public HexagonLabyrinth(int m, int n) {
        super(m, n);
        super.setSolver(new HexagonLabyrinthSolver());
        super.setCanvasGenerator(new HexagonLabyrinthCanvasGenerator());
        super.setGenerator(new HexagonLabyrinthGenerator());
        super.addVerticalWalls(m, n);
        super.addAscendingDiagonalWalls(m, n);
        super.addDescendingDiagonalWalls(m, n);
        this.uf = new UnionFind((m * n)*2);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n+1; j++) {
                verticalWalls[i][j] = true;
            }
        }

        for (int i = 0; i < m+1; i++) {
            for (int j = 0; j < n; j++) {
                ascendingDiagonalWalls[i][j] = true;
            }
        }

        for (int i = 0; i < m+1; i++) {
            for (int j = 0; j < n; j++) {
                descendingDiagonalWalls[i][j] = true;
            }
        }

    }

    public int getEntry() {
        return start;
    }

    public int getExit() {
        return end;
    }

    @Override
    public boolean isMovePossible(int x, int y, int newX, int newY) {
        // Vérifier les limites du labyrinthe
        if (newX < 0 || newY < 0 || newX >= verticalWalls.length || newY >= verticalWalls[0].length) {
            return false; // Le mouvement est en dehors des limites du labyrinthe
        }

        // Déplacement Est-Ouest (horizontal)
        if (newY == y) {
            if (newX > x) {
                return !verticalWalls[x][y]; // Pas de mur vertical à droite
            } else if (newX < x) {
                return !verticalWalls[x - 1][y]; // Pas de mur vertical à gauche
            }
        }

        // Déplacement Nord-Est / Sud-Ouest
        if (newX - newY == x - y) {
            if (newY > y) {
                return !ascendingDiagonalWalls[x][y]; // Pas de mur diagonal ascendant en bas
            } else if (newY < y) {
                return !ascendingDiagonalWalls[x - 1][y - 1]; // Pas de mur diagonal ascendant en haut
            }
        }

        // Déplacement Nord-Ouest / Sud-Est
        if (newX + newY == x + y) {
            if (newY > y) {
                return !descendingDiagonalWalls[x][y]; // Pas de mur diagonal descendant en bas
            } else if (newY < y) {
                return !descendingDiagonalWalls[x][y - 1]; // Pas de mur diagonal descendant en haut
            }
        }

        return false; // Par défaut, le mouvement n'est pas possible
    }



}
