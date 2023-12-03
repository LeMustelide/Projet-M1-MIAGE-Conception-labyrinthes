package labyrinth.labyrinthCanvasGenerator;

import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import labyrinth.LabyrinthBase;

import java.util.List;

public class SquareLabyrinthCanvasGenerator implements ILabyrinthCanvasGenerator{
    private int cellSize = 20;  // Taille de chaque cellule en pixels

    public void generateCanvas(LabyrinthBase labyrinth, GraphicsContext gc) {
        boolean[][] hWalls;
        boolean[][] vWalls;
        hWalls = labyrinth.getHorizontalWalls();
        vWalls = labyrinth.getVerticalWalls();
        drawWalls(gc, hWalls, vWalls);
        drawEntryAndExit(gc, labyrinth.getStart(), labyrinth.getEnd(), hWalls);
    }

    private void drawWalls(GraphicsContext gc, boolean[][] hWalls, boolean[][] vWalls) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        // Dessiner les murs verticaux
        for (int i = 0; i < vWalls.length; i++) {
            for (int j = 0; j < vWalls[i].length; j++) {
                if (vWalls[i][j]) {
                    int x = j * cellSize;
                    int y = i * cellSize;
                    gc.strokeLine(x, y, x, y + cellSize);
                }
            }
        }

        // Dessiner les murs horizontaux
        for (int i = 0; i < hWalls.length; i++) {
            for (int j = 0; j < hWalls[i].length; j++) {
                if (hWalls[i][j]) {
                    int x = j * cellSize;
                    int y = i * cellSize;
                    gc.strokeLine(x, y, x + cellSize, y);
                }
            }
        }
    }


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

    private void drawEntryAndExit(GraphicsContext gc, int entry, int exit, boolean[][] hWalls) {
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(3);
        gc.strokeLine(0, entry * cellSize, 0, entry * cellSize + cellSize);

        gc.setStroke(Color.RED);
        int exitX = (hWalls[0].length - 1) * cellSize;
        gc.strokeLine(exitX + cellSize, exit * cellSize, exitX + cellSize, exit * cellSize + cellSize);
    }

}
