package labyrinth.facade;

import labyrinth.Labyrinth;

public class Facade implements IFacade{

    private Labyrinth labyrinth;

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
    public Labyrinth generate(int width, int height, long seed) {
        //LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        this.labyrinth = new Labyrinth(width, height);
        this.labyrinth.generatePerfectLabyrinth(seed);
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
