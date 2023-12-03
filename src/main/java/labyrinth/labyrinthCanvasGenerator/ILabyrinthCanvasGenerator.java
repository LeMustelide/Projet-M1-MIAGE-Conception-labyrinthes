package labyrinth.labyrinthCanvasGenerator;

import javafx.scene.canvas.GraphicsContext;
import labyrinth.LabyrinthBase;

import java.util.List;

public interface ILabyrinthCanvasGenerator {
    void generateCanvas(LabyrinthBase labyrinth, GraphicsContext gc);
    void drawPath(GraphicsContext gc, List<int[]> path);
}
