package labyrinth.facade;

import labyrinth.Labyrinth;
import labyrinth.LabyrinthCanvasGenerator;
import labyrinth.LabyrinthSVGGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Facade implements IFacade{

    private Labyrinth labyrinth;
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
}
