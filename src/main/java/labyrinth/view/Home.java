package labyrinth.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
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
        vue.setGraphicsContext(vue.canvas.getGraphicsContext2D());
        vue.getGraphicsContext().clearRect(0, 0, vue.canvas.getWidth(), vue.canvas.getHeight());
        vue.setController(controleur);
        vue.btnGenerate.setOnAction(vue::handleGenerate);
        vue.btnSolve.setOnAction(vue::handleSolve);
        vue.btnPlay.setOnAction(vue::handlePlay);
        vue.btn3D.setOnAction(vue::handle3D);

        vue.canvas.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Si le focus est perdu (newValue est false)
                vue.gameState = GameState.PAUSED;
                vue.btnPlay.setText("Resume");
                vue.btnPlay.setStyle("-fx-background-color: orange;");
            } else {
                vue.gameState = GameState.PLAYING;
                vue.btnPlay.setText("Pause");
                vue.btnPlay.setStyle("-fx-background-color: #32a930;");
            }
        });
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

    @FXML
    private Button btnPlay;

    @FXML
    private Button btn3D;

    private Main3D main3D;

    private GameState gameState = GameState.STOPPED;

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

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private int playerX = 0; // Position initiale X du joueur
    private int playerY = 0; // Position initiale Y du joueur

    public void show() {
        this.getStage().setScene(this.getScene());
        this.getStage().show();
    }

    private void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;
        System.out.println("newX = " + newX + ", newY = " + newY);
        // Vérifiez si le mouvement est possible
        if (controller.canMove(newX, newY)) {
            clearPlayer();
            playerX = newX;
            playerY = newY;
            drawPlayer();
        }

        controller.getLabyrinth().movePlayer(newX, newY);
        drawPlayer();
    }

    private void tpPlayer(int x, int y) {
        clearPlayer();
        playerX = x;
        playerY = y;
        controller.getLabyrinth().movePlayer(x, y);
        drawPlayer();
    }

    private void clearPlayer() {
        gc.clearRect(playerX * 20 + 5, playerY * 20 + 5, 10, 10); // Effacer le cercle du joueur
    }

    private void drawPlayer() {
        gc.setFill(Color.BLUE);
        gc.fillOval(playerX * 20 + 5, playerY * 20 + 5, 10, 10); // Dessinez un cercle pour représenter le joueur
    }

    private void handleGenerate(javafx.event.ActionEvent actionEvent) {
        Random random = new Random();
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if(size.getText().isEmpty()) size.setText("10");
        if(seed.getText().isEmpty()) seed.setText(random.nextInt(10000000) + "");
        controller.generate(Integer.parseInt(size.getText()), Long.parseLong(seed.getText()));
        this.gameState = GameState.STOPPED;
        btnPlay.setText("Play");
        btnPlay.setStyle("-fx-background-color: #32a930;");
    }

    private void handleSolve(javafx.event.ActionEvent actionEvent) {
        if(controller.getLabyrinth() == null) return;
        controller.solve(this.algoList.getValue().toString());
    }

    private void handlePlay(ActionEvent actionEvent) {
        if(controller.getLabyrinth() == null) return;
        switch (gameState) {
            case PLAYING:
                gameState = GameState.PAUSED;
                btnPlay.setText("Resume");
                btnPlay.setStyle("-fx-background-color: orange;");
                break;
            case PAUSED:
                gameState = GameState.PLAYING;
                tpPlayer(controller.getLabyrinth().getPlayerX(), controller.getLabyrinth().getPlayerY());
                drawPlayer();
                btnPlay.setText("Pause");
                btnPlay.setStyle("-fx-background-color: #32a930;");
                canvas.requestFocus();
                break;
            default:
                this.gameState = GameState.PLAYING;
                clearPlayer();
                this.playerX = controller.getLabyrinth().getPlayerX();
                this.playerY = controller.getLabyrinth().getPlayerY();
                drawPlayer();
                canvas.requestFocus();
                btnPlay.setText("Pause");
        }
        Timeline timeline = new Timeline();

        // Créer un KeyFrame et un KeyValue pour animer la largeur préférée du bouton
        KeyValue keyValue = new KeyValue(btn3D.prefWidthProperty(), 48);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1), keyValue); // Durée de 2 secondes

        // Ajouter le KeyFrame au Timeline
        timeline.getKeyFrames().add(keyFrame);

        // Démarrer l'animation
        timeline.play();

        // Démarrer l'animation
        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Z: movePlayer(0, -1); break;
                case S: movePlayer(0, 1); break;
                case Q: movePlayer(-1, 0); break;
                case D: movePlayer(1, 0); break;
            }
        });
    }

    private void handle3D(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            this.main3D = new Main3D(this.controller.getLabyrinth());

            // Ajout du gestionnaire de fermeture
            stage.setOnCloseRequest(event -> {
                this.main3D.close();
            });

            main3D.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}