package labyrinth.facade;

import labyrinth.SquareLabyrinth;

public interface IFacade {
    String generate(int width, int height);

    SquareLabyrinth generate(int width, int height, long seed);
    void solve(String algo);

    void play();

    void save(String labyrinth, String filename);

}
