package labyrinth.facade;

import labyrinth.LabyrinthGenerator;
import labyrinth.SquareLabyrinth;

public class Facade implements IFacade{

    private SquareLabyrinth labyrinth;

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
    public SquareLabyrinth generate(int width, int height, long seed) {
        //LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        this.labyrinth = new SquareLabyrinth(width, height, 4);
        LabyrinthGenerator generator = new LabyrinthGenerator();
        generator.generatePerfectLabyrinth(seed, this.labyrinth);
        return this.labyrinth;
    }

    @Override
    public void solve(String algo) {
        switch (algo) {
            case "RightHandAlgorithm":
                this.labyrinth.solveWithRightHandAlgorithm();
                break;
            case "DijkstraAlgorithm":
                this.labyrinth.solveWithDijkstraAlgorithm();
                break;
            default:
                this.labyrinth.solveWithRightHandAlgorithm();
                break;
        }
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
