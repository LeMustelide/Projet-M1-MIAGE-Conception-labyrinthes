package labyrinth;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main3D extends Application {

    private final Set<KeyCode> keysPressed = ConcurrentHashMap.newKeySet();
    private final List<Box> walls = new ArrayList<>();
    private final double cameraCollisionBoxSize = 1.0;
    private final double cullingDistance = 300.0;

    private final double moveSpeed = 0.3;

    private static final int CHUNK_SIZE = 100; // Définir la taille de votre chunk
    private Map<Point2D, Chunk> chunks = new HashMap<>(); // Structure pour stocker vos chunks

    private Group root = new Group();

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final double mouseSensitivity = 0.08;
    private static final double DRAG_THRESHOLD = 5;

    @Override
    public void start(Stage primaryStage) throws IOException {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        Scene scene = new Scene(root, 1020, 1020, true);
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.WHITE); // Vous pouvez ajuster la couleur selon vos besoins
        pointLight.setLayoutX(150); // Position en X
        pointLight.setLayoutY(100); // Position en Y
        pointLight.setTranslateZ(-400); // Position en Z

        scene.setFill(Color.WHITE);

        double centerX = 100; // Moyenne des x de tous les points du labyrinthe
        double centerY = 100; // Moyenne des y de tous les points du labyrinthe

        // Positionner la caméra
        camera.setTranslateX(centerX);
        camera.setTranslateY(centerY);
        camera.setTranslateZ(-2); // Vous devrez peut-être ajuster cette valeur
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);

        // Variables pour stocker les rotations
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(90);

        camera.getTransforms().addAll(rotateY, rotateX);

        scene.setCamera(camera);

        // Créer une lumière ambiante
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(Color.rgb(200, 200, 200, 0.1)); // Couleur blanche avec une certaine transparence pour adoucir

        // Ajouter la lumière ambiante à la scène
        root.getChildren().add(ambientLight);

        root.getChildren().add(pointLight);

        // Ajouter un éclairage


        // Matériau pour les murs
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GRAY);
        PhongMaterial materialEnter = new PhongMaterial();
        materialEnter.setDiffuseColor(Color.GREEN);
        PhongMaterial materialExit = new PhongMaterial();
        materialExit.setDiffuseColor(Color.RED);

        // Création des murs en 3D
        for (LineData line : getLines("./labyrinthe.svg")) {
            double length = Math.sqrt(Math.pow(line.x2 - line.x1, 2) + Math.pow(line.y2 - line.y1, 2));
            Box wall = new Box(length, 5, 10); // longueur, profondeur, hauteur
            switch (line.styleClass) {
                case "entry":
                    wall.setMaterial(materialEnter);
                    break;
                case "exit":
                    wall.setMaterial(materialExit);
                    break;
                default:
                    wall.setMaterial(material);
                    break;
            }

            // Calculer la position et la rotation du mur
            double angle = Math.toDegrees(Math.atan2(line.y2 - line.y1, line.x2 - line.x1));
            wall.setTranslateX((line.x1 + line.x2) / 2.0);
            wall.setTranslateY((line.y1 + line.y2) / 2.0);
            wall.setRotationAxis(Rotate.Z_AXIS);
            wall.setRotate(angle);

            int chunkX = (int) (wall.getTranslateX() / CHUNK_SIZE);
            int chunkY = (int) (wall.getTranslateY() / CHUNK_SIZE);
            Chunk chunk = getOrCreateChunk(chunkX, chunkY);
            chunk.addWall(wall);
        }

        primaryStage.setTitle("Labyrinthe 3D");

        scene.setOnKeyPressed(event -> {
            double changeX = 0;
            double changeY = 0;
            double angleYRadians = Math.toRadians(rotateY.getAngle());

            switch (event.getCode()) {
                case Z:
                    camera.setTranslateZ(camera.getTranslateZ() + moveSpeed);
                    break;
                case S:
                    camera.setTranslateZ(camera.getTranslateZ() - moveSpeed);
                    break;
            }

            // Calculer la future position de la caméra
            double futureX = camera.getTranslateX() + changeX;
            double futureY = camera.getTranslateY() + changeY;

            // Vérifier les collisions avec chaque mur
            boolean collision = walls.stream().anyMatch(wall -> {
                Box testBox = new Box(cameraCollisionBoxSize, cameraCollisionBoxSize, cameraCollisionBoxSize);
                testBox.setTranslateX(futureX);
                testBox.setTranslateY(futureY);
                return testBox.getBoundsInParent().intersects(wall.getBoundsInParent());
            });

            // Si aucune collision n'est détectée, déplacer la caméra
            if (!collision) { // true en debug !collision
                camera.setTranslateX(futureX);
                camera.setTranslateY(futureY);
            }

            updateWallVisibility(camera);
            updateChunksVisibility(camera);
        });

        primaryStage.setScene(scene);

        scene.setOnKeyPressed(event -> keysPressed.add(event.getCode()));
        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCameraPosition(camera, rotateY, rotateX);
            }
        }.start();

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = rotateX.getAngle();
            anchorAngleY = rotateY.getAngle();
        });

        // Détecter le mouvement de la souris tout en maintenant le bouton enfoncé
        scene.setOnMouseDragged(event -> {
            rotateX.setAngle(anchorAngleX - (anchorY - event.getSceneY()) * mouseSensitivity);
            rotateY.setAngle(anchorAngleY + (anchorX - event.getSceneX()) * mouseSensitivity);
        });

        updateWallVisibility(camera);
        updateChunksVisibility(camera);
    }
    private void updateCameraPosition(Camera camera, Rotate rotateY, Rotate rotateX) {
        double changeX = 0;
        double changeY = 0;
        double changeZ = 0;
        double angleYRadians = Math.toRadians(rotateY.getAngle());
        double rotationSpeed = 2.0;

        if (keysPressed.contains(KeyCode.Z)) {
            changeX += moveSpeed * Math.sin(angleYRadians);
            changeY += -moveSpeed * Math.cos(angleYRadians);
        }
        if (keysPressed.contains(KeyCode.S)) {
            changeX += -moveSpeed * Math.sin(angleYRadians);
            changeY += moveSpeed * Math.cos(angleYRadians);
        }
        if (keysPressed.contains(KeyCode.D)) {
            changeX += moveSpeed * Math.cos(angleYRadians);
            changeY += moveSpeed * Math.sin(angleYRadians);
        }
        if (keysPressed.contains(KeyCode.Q)) {
            changeX += -moveSpeed * Math.cos(angleYRadians);
            changeY += -moveSpeed * Math.sin(angleYRadians);
        }
        if (keysPressed.contains(KeyCode.A)) {
            //camera.setTranslateZ(camera.getTranslateZ() + moveSpeed);
            changeZ += moveSpeed;
        }
        if (keysPressed.contains(KeyCode.E)) {
            //camera.setTranslateZ(camera.getTranslateZ() - moveSpeed);
            changeZ -= moveSpeed;
        }
        if(keysPressed.contains(KeyCode.LEFT)){
            rotateY.setAngle(rotateY.getAngle() - rotationSpeed);
        }
        if(keysPressed.contains(KeyCode.RIGHT)){
            rotateY.setAngle(rotateY.getAngle() + rotationSpeed);
        }
        if(keysPressed.contains(KeyCode.UP)){
            rotateX.setAngle(rotateX.getAngle() + rotationSpeed);
        }
        if(keysPressed.contains(KeyCode.DOWN)){
            rotateX.setAngle(rotateX.getAngle() - rotationSpeed);
        }
        if(keysPressed.contains(KeyCode.SHIFT)){
            changeX *= 4;
            changeY *= 4;
            changeZ *= 4;
        }

        // Mettre à jour la position de la caméra si aucune collision n'est détectée
        moveCameraIfPossible(changeX, changeY, changeZ, camera);
    }

    private void moveCameraIfPossible(double changeX, double changeY, double changeZ, Camera camera) {
        double futureX = camera.getTranslateX() + changeX;
        double futureY = camera.getTranslateY() + changeY;
        double futureZ = camera.getTranslateZ() + changeZ;

        // Vérifier les collisions avec chaque mur
        boolean collision = getCurrentChunk(camera).walls.stream().anyMatch(wall -> {
            Box testBox = new Box(cameraCollisionBoxSize, cameraCollisionBoxSize, cameraCollisionBoxSize);
            testBox.setTranslateX(futureX);
            testBox.setTranslateY(futureY);
            testBox.setTranslateZ(futureZ);
            return testBox.getBoundsInParent().intersects(wall.getBoundsInParent());
        });

        // Si aucune collision n'est détectée, déplacer la caméra
        if (!collision) {

            camera.setTranslateX(futureX);
            camera.setTranslateY(futureY);
            camera.setTranslateZ(futureZ); // Add the elevation change here
        }

        updateWallVisibility(camera);
        updateChunksVisibility(camera);
    }

    private void updateWallVisibility(Camera camera) {
        Point3D cameraPosition = new Point3D(camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ());

        for (Box wall : walls) {
            Point3D wallPosition = new Point3D(wall.getTranslateX(), wall.getTranslateY(), wall.getTranslateZ());
            if (cameraPosition.distance(wallPosition) > cullingDistance) {
                wall.setVisible(false); // Cacher les murs trop loin
            } else {
                wall.setVisible(true); // Afficher les murs proches
            }
        }
    }

    // Méthode pour obtenir ou créer un chunk à partir de coordonnées
    private Chunk getOrCreateChunk(int x, int y) {
        Point2D chunkCoord = new Point2D(x, y);
        return chunks.computeIfAbsent(chunkCoord, k -> new Chunk());
    }

    // Méthode pour mettre à jour la visibilité des chunks basée sur la position de la caméra
    private void updateChunksVisibility(Camera camera) {
        // Coordonnées de la caméra
        double camX = camera.getTranslateX();
        double camY = camera.getTranslateY();

        // Calculer les indices des chunks dans lesquels la caméra se trouve actuellement
        int currentChunkX = (int) (camX / CHUNK_SIZE);
        int currentChunkY = (int) (camY / CHUNK_SIZE);

        //System.out.println("Current chunk: " + currentChunkX + ", " + currentChunkY);

        // Déterminer la portée des chunks à vérifier basée sur la distance de culling
        int viewDistance = (int) Math.ceil(cullingDistance / CHUNK_SIZE);

        // Itérer sur les chunks dans la portée de la caméra
        for (int x = currentChunkX - viewDistance; x <= currentChunkX + viewDistance; x++) {
            for (int y = currentChunkY - viewDistance; y <= currentChunkY + viewDistance; y++) {
                // Obtenir le chunk aux coordonnées actuelles
                Chunk chunk = getOrCreateChunk(x, y);

                // Vérifier si le chunk est dans la portée de la caméra
                boolean isVisible = isChunkInView(camera, chunk, x, y);

                // Mettre à jour la visibilité du chunk
                chunk.setVisible(isVisible);
            }
        }
    }

    // Méthode qui retourne le chunk actuel
    private Chunk getCurrentChunk(Camera camera) {
        // Coordonnées de la caméra
        double camX = camera.getTranslateX();
        double camY = camera.getTranslateY();

        // Calculer les indices des chunks dans lesquels la caméra se trouve actuellement
        int currentChunkX = (int) (camX / CHUNK_SIZE);
        int currentChunkY = (int) (camY / CHUNK_SIZE);

        // Obtenir le chunk aux coordonnées actuelles
        return getOrCreateChunk(currentChunkX, currentChunkY);
    }


    private boolean isChunkInView(Camera camera, Chunk chunk, int chunkX, int chunkY) {
        // Coordonnées du centre du chunk
        double centerX = (chunkX * CHUNK_SIZE) + CHUNK_SIZE / 2.0;
        double centerY = (chunkY * CHUNK_SIZE) + CHUNK_SIZE / 2.0;

        // Distance entre la caméra et le centre du chunk
        double distance = Math.hypot(centerX - camera.getTranslateX(), centerY - camera.getTranslateY());

        // Le chunk est considéré comme visible si la distance est inférieure à la distance de culling
        return distance < cullingDistance;
    }

    // Classe pour représenter un chunk
    class Chunk {
        private List<Box> walls = new ArrayList<>();

        void addWall(Box wall) {
            walls.add(wall);
        }

        void setVisible(boolean visible) {
            for (Box wall : walls) {
                if (!root.getChildren().contains(wall)) {
                    root.getChildren().add(wall);
                }
                wall.setVisible(visible);
            }
        }
    }

    private List<LineData> getLines(String svgFilePath) throws IOException {
        // Lire le contenu du fichier SVG
        String content = new String(Files.readAllBytes(Paths.get(svgFilePath)));

        // Créer une liste pour stocker les données des lignes
        List<LineData> lines = new ArrayList<>();

        // Utiliser une expression régulière pour extraire les informations des lignes
        Pattern pattern = Pattern.compile("<line x1=\"(\\d+)\" y1=\"(\\d+)\" x2=\"(\\d+)\" y2=\"(\\d+)\"\\s*/>");
        Matcher matcher = pattern.matcher(content);
        Pattern patternEnter = Pattern.compile("<line class=\"entry\" x1=\"(\\d+)\" y1=\"(\\d+)\" x2=\"(\\d+)\" y2=\"(\\d+)\"\\s*/>");
        Matcher matcherEnter = patternEnter.matcher(content);
        Pattern patternExit = Pattern.compile("<line class=\"exit\" x1=\"(\\d+)\" y1=\"(\\d+)\" x2=\"(\\d+)\" y2=\"(\\d+)\"\\s*/>");
        Matcher matcherExit = patternExit.matcher(content);

        // Trouver toutes les correspondances et ajouter les données à la liste
        while (matcher.find()) {
            double x1 = Double.parseDouble(matcher.group(1));
            double y1 = Double.parseDouble(matcher.group(2));
            double x2 = Double.parseDouble(matcher.group(3));
            double y2 = Double.parseDouble(matcher.group(4));
            lines.add(new LineData(x1, y1, x2, y2));
        }

        while (matcherEnter.find()) {
            double x1 = Double.parseDouble(matcherEnter.group(1));
            double y1 = Double.parseDouble(matcherEnter.group(2));
            double x2 = Double.parseDouble(matcherEnter.group(3));
            double y2 = Double.parseDouble(matcherEnter.group(4));
            lines.add(new LineData(x1, y1, x2, y2, "entry"));
        }

        while (matcherExit.find()) {
            double x1 = Double.parseDouble(matcherExit.group(1));
            double y1 = Double.parseDouble(matcherExit.group(2));
            double x2 = Double.parseDouble(matcherExit.group(3));
            double y2 = Double.parseDouble(matcherExit.group(4));
            lines.add(new LineData(x1, y1, x2, y2, "exit"));
        }

        return lines;
    }

    public static void main(String[] args) {
        launch(args);
    }

    class LineData {
        double x1, y1, x2, y2;

        String styleClass = "";

        public LineData(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public LineData(double x1, double y1, double x2, double y2, String styleClass) {
            this(x1, y1, x2, y2);
            this.styleClass = styleClass;
        }
    }
}