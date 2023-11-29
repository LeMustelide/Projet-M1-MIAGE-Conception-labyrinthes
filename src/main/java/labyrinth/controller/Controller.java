package labyrinth.controller;

import javafx.stage.Stage;
import labyrinth.SquareLabyrinth;
import labyrinth.view.LabyrinthCanvasGenerator;
import labyrinth.facade.Facade;
import labyrinth.view.Home;

public class Controller {

    private Home home;
    private Facade facade;
    private LabyrinthCanvasGenerator generator;
    private SquareLabyrinth labyrinth;
    public Controller(Facade facade, Stage stage) {
        this.facade = facade;
        this.generator = new LabyrinthCanvasGenerator();
        home = Home.creerVue(this, stage);
    }

    public void showHome() {
        home.show();
    }

    public void run() {
        showHome();
    }

    public void generate(int sizex, int sizey, long seed) {
        labyrinth = facade.generate(sizex, sizey, seed);
        home.getGraphicsContext().getCanvas().setWidth(sizey * 20);
        home.getGraphicsContext().getCanvas().setHeight(sizex * 20);
        generator.generateCanvas(labyrinth, home.getGraphicsContext());
    }

    public void regenerate() {
        generator.generateCanvas(labyrinth, home.getGraphicsContext());
    }

    public void solve(String algo) {
        facade.solve(algo);
        generator.drawPath(home.getGraphicsContext(), labyrinth.getPath());
    }

    public boolean canMove(int x, int y) {
        return labyrinth.isMovePossible2D(x, y);
    }

    public SquareLabyrinth getLabyrinth() {
        return labyrinth;
    }
}
