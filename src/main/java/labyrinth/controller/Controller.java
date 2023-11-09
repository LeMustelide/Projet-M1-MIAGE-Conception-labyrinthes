package labyrinth.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import labyrinth.Labyrinth;
import labyrinth.LabyrinthCanvasGenerator;
import labyrinth.facade.Facade;
import labyrinth.view.Home;

public class Controller {

    private Home home;
    private Facade facade;
    private LabyrinthCanvasGenerator generator;
    private Labyrinth labyrinth;
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

    public void generate(int size, long seed) {
        labyrinth = facade.generate(size, size, seed);
        home.getGraphicsContext().getCanvas().setWidth(size * 20);
        home.getGraphicsContext().getCanvas().setHeight(size * 20);
        generator.generateCanvas(labyrinth, home.getGraphicsContext());
        // Je voudrais redimensionner le canvas en fonction de la taille du labyrinthe

    }

    public void solve(String algo) {
        facade.solve(algo);
        generator.drawPath(home.getGraphicsContext(), labyrinth.getPath());
    }
}
