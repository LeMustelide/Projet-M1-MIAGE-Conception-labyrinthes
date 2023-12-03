package labyrinth.facade;

import labyrinth.*;
import labyrinth.generator.SquareLabyrinthGenerator;
import labyrinth.model.LabyrinthSVGGenerator;
import labyrinth.solver.ILabyrinthSolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Facade implements IFacade{

    private LabyrinthBase labyrinth;

    private static Facade instance = new Facade();

    private Facade() {
    }

    @Override
    public String generate(int width, int height, String shape) {
        long seed = 10;
        generate(width, height, shape);
        return String.valueOf(seed);
    }

    @Override
    public LabyrinthBase generate(int width, int height, long seed, String shape) {
        //LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        switch (shape) {
            case "Square":
                this.labyrinth = new SquareLabyrinth(width, height);
                break;
            case "Hexagon":
                this.labyrinth = new HexagonLabyrinth(width, height);
                break;
            default:
                this.labyrinth = new SquareLabyrinth(width, height);
                break;
        }
        //this.labyrinth = new SquareLabyrinth(width, height);
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

    public void download(SquareLabyrinth labyrinth) {
        LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        String svg = generator.generateSVG(labyrinth, labyrinth.getSolver().getPath());
        try {
            File myObj = new File("labyrinthe.svg");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());

            } else {
                System.out.println("Updated file.");
            }
            FileOutputStream fos = new FileOutputStream("labyrinthe.svg");
            fos.write(svg.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
