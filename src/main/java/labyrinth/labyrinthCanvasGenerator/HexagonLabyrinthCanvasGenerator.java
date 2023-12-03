package labyrinth.labyrinthCanvasGenerator;

import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import labyrinth.LabyrinthBase;

import java.util.List;

public class HexagonLabyrinthCanvasGenerator implements ILabyrinthCanvasGenerator{
    private int cellSize = 20;  // Taille de chaque cellule en pixels
    @Override
    public void generateCanvas(LabyrinthBase labyrinth, GraphicsContext gc) {
        boolean[][] vWalls;
        boolean[][] adWalls;
        boolean[][] ddWalls;
        vWalls = labyrinth.getVerticalWalls();
        adWalls = labyrinth.getAscendingDiagonalWalls();
        ddWalls = labyrinth.getDescendingDiagonalWalls();
        System.out.println(vWalls.length + " " + (vWalls[0].length-1));
        drawWallsHexagonal(gc, vWalls, adWalls, ddWalls, 20, vWalls.length, vWalls[0].length-1);
    }

    @Override
    public void drawPath(GraphicsContext gc, List<int[]> path) {
        if (path != null && path.size() > 0) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);

            for (int i = 0; i < path.size() - 1; i++) {
                int x1 = path.get(i)[0] * cellSize + cellSize / 2;
                int y1 = path.get(i)[1] * cellSize + cellSize / 2;
                int x2 = path.get(i + 1)[0] * cellSize + cellSize / 2;
                int y2 = path.get(i + 1)[1] * cellSize + cellSize / 2;
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawWallsHexagonal(GraphicsContext gc, boolean[][] vWalls, boolean[][] adWalls, boolean[][] ddWalls, double hexSize, int numRows, int numCols) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {

                double y = j * Math.sqrt(3) * hexSize * 0.9 + hexSize;
                double x = i * Math.sqrt(3) * hexSize + hexSize;
                if (j % 2 == 1) {
                    x += hexSize * Math.sqrt(3) / 2;
                }

                // Crée un hexagone pour chaque cellule du labyrinthe
                Polygon hexagon = createHexagon(x, y, hexSize);

                // Colorie les hexagones en fonction de la présence de murs
                //hexagon.setFill(Color.BLACK);
                gc.setStroke(Color.BLACK);
                ObservableList<Double> points = hexagon.getPoints();

                if (vWalls[i][j]) {
                    gc.strokeLine(points.get(4).doubleValue(), points.get(5).doubleValue(), points.get(6).doubleValue(), points.get(7).doubleValue());//gauche
                }
                if (adWalls[i][j]) {
                    gc.strokeLine(points.get(6).doubleValue(), points.get(7).doubleValue(), points.get(8).doubleValue(), points.get(9).doubleValue());//hautgauche
                }
                if (ddWalls[i][j]) {
                    gc.strokeLine(points.get(8).doubleValue(), points.get(9).doubleValue(), points.get(10).doubleValue(), points.get(11).doubleValue());//hautdroite
                }


                // affichage des bordures

                if (i == 0 && j % 2 == 0 || j == numRows - 1) {
                    gc.strokeLine(points.get(2), points.get(3), points.get(4), points.get(5));//basgauche
                }
                if (i == numCols - 1) {
                    gc.strokeLine(points.get(10), points.get(11), points.get(0), points.get(1));//droite
                }
                if (i == numRows - 1 && j % 2 == 1 || j == numCols - 1) {
                    gc.strokeLine(points.get(0), points.get(1), points.get(2), points.get(3));//basdroite
                }
            }
        }
    }

    private Polygon createHexagon(double x, double y, double size) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI / 6 * (i+0.5);
            double xOffset = size * Math.cos(angle);
            double yOffset = size * Math.sin(angle);
            hexagon.getPoints().addAll(x + xOffset, y + yOffset);
        }
        return hexagon;
    }
}
