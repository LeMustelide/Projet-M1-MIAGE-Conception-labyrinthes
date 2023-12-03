package labyrinth.solver;

import labyrinth.LabyrinthBase;
import java.util.List;

public interface ILabyrinthSolver {

    List<int[]> solveWithDijkstraAlgorithm(LabyrinthBase labyrinth);

    List<int[]> solveWithRightHandAlgorithm(LabyrinthBase labyrinth);

    List<int[]> getPath();
}
