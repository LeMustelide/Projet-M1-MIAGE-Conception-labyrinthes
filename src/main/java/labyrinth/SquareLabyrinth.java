package labyrinth;

import labyrinth.generator.SquareLabyrinthGenerator;
import labyrinth.labyrinthCanvasGenerator.SquareLabyrinthCanvasGenerator;
import labyrinth.solver.SquareLabyrinthSolver;
import labyrinth.view.Shape;

import java.util.*;

public class SquareLabyrinth extends LabyrinthBase {

    private Shape shape;

    public SquareLabyrinth(int m, int n) {
        super(m, n);
        super.setSolver(new SquareLabyrinthSolver());
        super.setCanvasGenerator(new SquareLabyrinthCanvasGenerator());
        super.setGenerator(new SquareLabyrinthGenerator());
        super.addVerticalWalls(m, n);
        super.addHorizontalWalls(m, n);
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

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

}
