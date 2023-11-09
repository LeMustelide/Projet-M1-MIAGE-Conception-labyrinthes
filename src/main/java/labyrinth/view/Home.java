package labyrinth.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import labyrinth.controller.Controller;

import java.io.IOException;
import java.util.Random;

public class Home {

    public static Home creerVue(Controller controleur, Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(Home.class.
                getResource("/view/Home.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Home vue = fxmlLoader.getController();
        vue.setStage(stage);
        vue.setScene(new Scene(vue.getTop()));
        //LoadLabyrinthImage loadLabyrinthImage = new LoadLabyrinthImage();
        //loadLabyrinthImage.setSVGImage("C:\\Users\\firef\\Desktop\\Git\\Projet-M1-MIAGE-Conception-labyrinthes\\labyrinthe.svg", vue.getLabyrinthImage());
        vue.setGraphicsContext(vue.canvas.getGraphicsContext2D());
        vue.getGraphicsContext().clearRect(0, 0, vue.canvas.getWidth(), vue.canvas.getHeight());

        vue.setController(controleur);
        vue.btnGenerate.setOnAction(vue::handleGenerate);
        vue.btnSolve.setOnAction(vue::handleSolve);
        vue.size.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                // Si le nouveau texte n'est pas numérique ou dépasse la longueur maximale, rétablissez l'ancienne valeur
                if (!newValue.matches("\\d*") || newValue.length() > 4) {
                    vue.size.setText(oldValue);
                }
            }
        });
        vue.seed.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Si le nouveau texte n'est pas numérique ou dépasse la longueur maximale, rétablissez l'ancienne valeur
                if (!newValue.matches("\\d*") || newValue.length() > 18) {
                    vue.seed.setText(oldValue);
                }
            }
        });

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "RightHandAlgorithm",
                        "DijkstraAlgorithm"
                );

        vue.algoList.setItems(options);
        vue.algoList.setValue("RightHandAlgorithm");

        return vue;
    }

    @FXML
    private VBox vBox;

    @FXML
    private Canvas canvas;

    @FXML
    private Button btnGenerate;

    @FXML
    private TextField size;

    @FXML
    private TextField seed;

    @FXML
    private ComboBox algoList;

    @FXML
    private Button btnSolve;

    private GraphicsContext gc;

    private Stage stage;

    private Scene scene;

    private Controller controller;

    public Parent getTop() {
        return vBox;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public Canvas getLabyrinthImage() {
        return canvas;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void show() {
        this.getStage().setScene(this.getScene());
        this.getStage().show();
    }

    private void handleGenerate(javafx.event.ActionEvent actionEvent) {
        Random random = new Random();
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if(size.getText().isEmpty()) size.setText("10");
        if(seed.getText().isEmpty()) seed.setText(random.nextInt(10000000) + "");
        controller.generate(Integer.valueOf(size.getText()), Long.valueOf(seed.getText()));
    }

    private void handleSolve(javafx.event.ActionEvent actionEvent) {
        controller.solve(this.algoList.getValue().toString());
    }
}
