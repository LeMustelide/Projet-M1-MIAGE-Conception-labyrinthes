package labyrinth;

import javafx.application.Application;
import javafx.stage.Stage;
import labyrinth.controller.Controller;
import labyrinth.facade.Facade;

import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        stage.setMaximized(true);
        Controller controller = new Controller(Facade.getInstance(), stage);
        controller.run();
    }
    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    /*
    public static void main(String[] args) {
        int size = 1000;

        if (args.length > 0) {
            try {
                size = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid size provided. Using default size of 10.");
            }
        }

        LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        Labyrinth labyrinth = new Labyrinth(size, size);
        labyrinth.generatePerfectLabyrinth();
        String svg = generator.generateSVG(labyrinth);
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
    */
}
