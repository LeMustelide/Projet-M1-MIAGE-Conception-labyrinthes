module Projet.M1.MIAGE.Conception.labyrinthes {
    requires javafx.controls;
    requires javafx.fxml;
    requires batik.transcoder;

    opens labyrinth.view to javafx.fxml;
    exports labyrinth;
}