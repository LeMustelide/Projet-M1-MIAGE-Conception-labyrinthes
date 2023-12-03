package labyrinth.facade;

import labyrinth.LabyrinthBase;
import labyrinth.SquareLabyrinth;

import java.util.List;

public interface IFacade {
    String generate(int width, int height);

    LabyrinthBase generate(int width, int height, long seed);
    List<int[]> solve(String algo);

    void play();

    void save(String labyrinth, String filename);

}
