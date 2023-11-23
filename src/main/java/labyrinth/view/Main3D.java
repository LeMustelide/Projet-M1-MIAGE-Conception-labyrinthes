package labyrinth.view;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import labyrinth.Labyrinth;

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

    private AnimationTimer handleInput;
    private final double moveSpeed = 0.3;

    private static final int CHUNK_SIZE = 100; // Définir la taille de votre chunk
    private Map<Point2D, Chunk> chunks = new HashMap<>(); // Structure pour stocker vos chunks

    private Group root = new Group();
    private boolean gridVisible = false;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final double mouseSensitivityX = 0.06;
    private final double mouseSensitivityY = 0.03;

    private Text fpsText = new Text();
    private long lastUpdate = 0;
    private boolean fpsVisible = false;

    private Labyrinth labyrinth;

    public Main3D(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }

    public Main3D() {}

    @Override
    public void start(Stage primaryStage) throws IOException {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        primaryStage.setMaximized(true);
        Scene scene = new Scene(root, 1020, 1020, true);


        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.WHITE); // Vous pouvez ajuster la couleur selon vos besoins
        pointLight.setLayoutX(150); // Position en X
        pointLight.setLayoutY(100); // Position en Y
        pointLight.setTranslateZ(-400); // Position en Z

        scene.setFill(Color.WHITE);

        // Positionner la caméra
        camera.setTranslateX(20);
        camera.setTranslateY(20);
        //camera.setTranslateZ(20);
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
        for (LineData line : this.labyrinth != null ? getLines(this.labyrinth) : getLines("src/main/resources/labyrinth.svg")) {
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
            Chunk chunk = getOrCreateChunk(chunkX, chunkY); // OK
            chunk.addWall(wall);
        }

        if(this.labyrinth != null) {
            camera.setTranslateX(this.labyrinth.getPlayerX() * 20 + 5);
            camera.setTranslateY(this.labyrinth.getPlayerY() * 20 + 5);
        }

        primaryStage.setTitle("Labyrinthe 3D");

        primaryStage.setScene(scene);

        scene.setOnKeyPressed(
                event -> {
                    keysPressed.add(event.getCode());
                    if (event.isControlDown() && event.getCode() == KeyCode.H) {
                        toggleGrid();
                    }
                }
        );
        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
        primaryStage.show();

        handleInput = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCameraPosition(camera, rotateY, rotateX);
            }
        };
        handleInput.start();

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = rotateX.getAngle();
            anchorAngleY = rotateY.getAngle();
        });

        // Détecter le mouvement de la souris tout en maintenant le bouton enfoncé
        scene.setOnMouseDragged(event -> {
            rotateX.setAngle(anchorAngleX - (anchorY - event.getSceneY()) * mouseSensitivityX);
            rotateY.setAngle(anchorAngleY + (anchorX - event.getSceneX()) * mouseSensitivityY);
        });

        updateWallVisibility(camera);
        updateChunksVisibility(camera);
    }

    private void toggleGrid() {
        if (gridVisible) {
            root.getChildren().removeIf(node -> "chunkGrid".equals(node.getId()));
            gridVisible = false;
        } else {
            drawChunckLimit();
            gridVisible = true;
        }
    }
    private void updateCameraPosition(Camera camera, Rotate rotateY, Rotate rotateX) {
        double changeX = 0;
        double changeY = 0;
        double changeZ = 0;
        double angleYRadians = Math.toRadians(rotateY.getAngle());
        double rotationSpeedX = 0.8;
        double rotationSpeedY = 1.2;


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
            rotateY.setAngle(rotateY.getAngle() - rotationSpeedY);
        }
        if(keysPressed.contains(KeyCode.RIGHT)){
            rotateY.setAngle(rotateY.getAngle() + rotationSpeedY);
        }
        if(keysPressed.contains(KeyCode.UP)){
            rotateX.setAngle(rotateX.getAngle() + rotationSpeedX);
        }
        if(keysPressed.contains(KeyCode.DOWN)){
            rotateX.setAngle(rotateX.getAngle() - rotationSpeedX);
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

        Chunk currentChunk = getCurrentChunk(camera);

        // Identifier les chunks adjacents
        Set<Chunk> adjacentChunks = getAdjacentChunks(camera);

        // Ajouter le chunk actuel à l'ensemble des chunks à vérifier
        adjacentChunks.add(currentChunk);

        // Vérifier les collisions dans tous les chunks pertinents
        boolean collision = adjacentChunks.stream().anyMatch(chunk -> chunk.walls.stream().anyMatch(wall -> {
            Box testBox = new Box(cameraCollisionBoxSize, cameraCollisionBoxSize, cameraCollisionBoxSize);
            testBox.setTranslateX(futureX);
            testBox.setTranslateY(futureY);
            testBox.setTranslateZ(futureZ);
            return testBox.getBoundsInParent().intersects(wall.getBoundsInParent());
        }));

        // Si aucune collision n'est détectée, déplacer la caméra
        if (!collision) {
            camera.setTranslateX(futureX);
            camera.setTranslateY(futureY);
            camera.setTranslateZ(futureZ); // Add the elevation change here
            this.labyrinth.setPlayerPosition((int) (futureX / 20), (int) (futureY / 20));
        }

        updateWallVisibility(camera);
        updateChunksVisibility(camera);
    }

    private Set<Chunk> getAdjacentChunks(Camera camera) {
        Set<Chunk> adjacentChunks = new HashSet<>();

        // Coordonnées de la caméra
        double camX = camera.getTranslateX();
        double camY = camera.getTranslateY();

        // Calculer les indices des chunks dans lesquels la caméra se trouve actuellement
        int currentChunkX = (int) (camX / CHUNK_SIZE);
        int currentChunkY = (int) (camY / CHUNK_SIZE);

        // Ajouter le chunk actuel et les chunks adjacents
        for (int x = currentChunkX - 1; x <= currentChunkX + 1; x++) {
            for (int y = currentChunkY - 1; y <= currentChunkY + 1; y++) {
                // Vérifiez que le chunk existe avant de l'ajouter
                Chunk chunk = getChunk(x, y);
                if (chunk != null) {
                    adjacentChunks.add(chunk);
                }
            }
        }

        return adjacentChunks;
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
        return chunks.computeIfAbsent(chunkCoord, k -> {
            return new Chunk();
        } );
    }

    private Chunk getChunk(int x, int y) {
        Point2D chunkCoord = new Point2D(x, y);
        return chunks.get(chunkCoord);
    }

    // Méthode pour mettre à jour la visibilité des chunks basée sur la position de la caméra
    private void updateChunksVisibility(Camera camera) {
        // Coordonnées de la caméra
        double camX = camera.getTranslateX();
        double camY = camera.getTranslateY();

        // Calculer les indices des chunks dans lesquels la caméra se trouve actuellement
        int currentChunkX = (int) (camX / CHUNK_SIZE);
        int currentChunkY = (int) (camY / CHUNK_SIZE);

        // Déterminer la portée des chunks à vérifier basée sur la distance de culling
        int viewDistance = (int) Math.ceil(cullingDistance / CHUNK_SIZE);

        // Itérer sur les chunks dans la portée de la caméra
        for (int x = Math.max(currentChunkX - viewDistance, 0); x <= currentChunkX + viewDistance; x++) {
            for (int y = Math.max(currentChunkY - viewDistance, 0); y <= currentChunkY + viewDistance; y++) {
                // Obtenir le chunk aux coordonnées actuelles

                Chunk chunk = getChunk(x, y); // OK

                // Vérifier si le chunk est dans la portée de la caméra
                if(chunk == null) continue;
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
        return getOrCreateChunk(currentChunkX, currentChunkY); // OK
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

    public List<LineData> getLines(Labyrinth labyrinth) {
        List<LineData> lines = new ArrayList<>();
        double cellSize = 20;

        boolean[][] hWalls = labyrinth.getHorizontalWalls();
        boolean[][] vWalls = labyrinth.getVerticalWalls();

        // Parcourir les murs horizontaux
        for (int y = 0; y < hWalls.length; y++) {
            for (int x = 0; x < hWalls[y].length; x++) {
                if (hWalls[y][x]) {
                    // Ajouter une ligne pour le mur horizontal
                    double yPosition = y * cellSize;
                    lines.add(new LineData(x * cellSize, yPosition, (x + 1) * cellSize, yPosition));
                }
            }
        }

        // Parcourir les murs verticaux
        for (int y = 0; y < vWalls.length; y++) {
            for (int x = 0; x < vWalls[y].length; x++) {
                if (vWalls[y][x]) {
                    // Ajouter une ligne pour le mur vertical
                    double xPosition = x * cellSize;
                    lines.add(new LineData(xPosition, y * cellSize, xPosition, (y + 1) * cellSize));
                }
            }
        }

        return lines;
    }

    private void drawChunckLimit() {
        double gridHeight = 255;
        double size = Math.sqrt(this.chunks.size());
        for (int x = 0; x <= size * CHUNK_SIZE; x += CHUNK_SIZE) {
            Line line = new Line(x, 0, x, size * CHUNK_SIZE);
            line.setStroke(Color.RED);
            line.setId("chunkGrid"); // Pour identifier facilement les lignes du cadrillage
            root.getChildren().add(line);
        }

        for (int y = 0; y <= size * CHUNK_SIZE; y += CHUNK_SIZE) {
            Line line = new Line(0, y, size * CHUNK_SIZE, y);
            line.setStroke(Color.RED);
            line.setId("chunkGrid");
            root.getChildren().add(line);
        }

        for (int x = 0; x <= size * CHUNK_SIZE; x += CHUNK_SIZE) {
            for (int y = 0; y <= size * CHUNK_SIZE; y += CHUNK_SIZE) {
                Box verticalLine = new Box(0.1, 0.1, gridHeight); // Utilisez Box avec une très petite largeur et profondeur
                verticalLine.setTranslateX(x);
                verticalLine.setTranslateY(y);
                verticalLine.setTranslateZ((gridHeight / 2) * -1); // Positionner la boîte au milieu de sa hauteur
                styleGridLine(verticalLine);
                root.getChildren().add(verticalLine);
            }
        }
    }

    private void styleGridLine(Shape3D line) {
        line.setMaterial(new PhongMaterial(Color.RED));
        line.setId("chunkGrid"); // Pour identifier facilement les lignes du cadrillage
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

    public void close() {
        handleInput.stop();
    }

}