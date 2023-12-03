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

}
