package labyrinth.controller;

import javafx.stage.Stage;
import labyrinth.HexagonLabyrinth;
import labyrinth.LabyrinthBase;
import labyrinth.SquareLabyrinth;
import labyrinth.labyrinthCanvasGenerator.ILabyrinthCanvasGenerator;
import labyrinth.labyrinthCanvasGenerator.SquareLabyrinthCanvasGenerator;
import labyrinth.facade.Facade;
import labyrinth.view.Home;

public class Controller {

    private Home home;
    private Facade facade;
    private LabyrinthBase labyrinth;
    public Controller(Facade facade, Stage stage) {
        this.facade = facade;
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
        if(labyrinth instanceof SquareLabyrinth) {
            home.getGraphicsContext().getCanvas().setWidth(sizey * 20);
            home.getGraphicsContext().getCanvas().setHeight(sizex * 20);
        }
        else if(labyrinth instanceof HexagonLabyrinth) {
            home.getGraphicsContext().getCanvas().setWidth(sizey * 40 + 20);
            home.getGraphicsContext().getCanvas().setHeight(sizex * 35 + 20);
        }
        ILabyrinthCanvasGenerator generator = labyrinth.getCanvasGenerator();
        generator.generateCanvas(labyrinth, home.getGraphicsContext());
    }

    public void regenerate() {
        labyrinth.getCanvasGenerator().generateCanvas(labyrinth, home.getGraphicsContext());
    }

    public void solve(String algo) {
        labyrinth.getCanvasGenerator().drawPath(home.getGraphicsContext(), facade.solve(algo));
    }

    public boolean canMove(int x, int y) {
        return labyrinth.isMovePossible2D(x, y);
    }

    public LabyrinthBase getLabyrinth() {
        return labyrinth;
    }
}
