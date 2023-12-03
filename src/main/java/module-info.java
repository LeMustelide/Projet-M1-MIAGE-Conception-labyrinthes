module Projet.M1.MIAGE.Conception.labyrinthes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens labyrinth.view to javafx.fxml;
    exports labyrinth.view;
    exports labyrinth.solver;
    opens labyrinth.solver to javafx.fxml;
    exports labyrinth.labyrinthCanvasGenerator;
    opens labyrinth.labyrinthCanvasGenerator to javafx.fxml;
}