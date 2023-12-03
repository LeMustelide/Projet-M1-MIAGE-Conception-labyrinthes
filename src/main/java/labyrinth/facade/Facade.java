package labyrinth.facade;

import labyrinth.*;
import labyrinth.generator.SquareLabyrinthGenerator;
import labyrinth.solver.ILabyrinthSolver;

import java.util.List;

public class Facade implements IFacade{

    private LabyrinthBase labyrinth;

    private static Facade instance = new Facade();

    private Facade() {
    }

    @Override
    public String generate(int width, int height) {
        long seed = 10;
        generate(width, height);
        return String.valueOf(seed);
    }

    @Override
    public LabyrinthBase generate(int width, int height, long seed) {
        //LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        //this.labyrinth = new SquareLabyrinth(width, height);
        this.labyrinth = new HexagonLabyrinth(width, height);
        this.labyrinth.getGenerator().generatePerfectLabyrinth(seed, this.labyrinth);
        return this.labyrinth;
    }

    @Override
    public List<int[]> solve(String algo) {
        ILabyrinthSolver solver = this.labyrinth.getSolver();
        if (algo.equals("DijkstraAlgorithm")) {
            solver.solveWithDijkstraAlgorithm(this.labyrinth);
        } else {
            solver.solveWithRightHandAlgorithm(this.labyrinth);
        }
        return solver.getPath();
    }

    @Override
    public void play() {

    }

    @Override
    public void save(String labyrinth, String filename) {

    }

    public static Facade getInstance() {
        return instance;
    }
}
