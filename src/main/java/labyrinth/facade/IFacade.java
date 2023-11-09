package labyrinth.facade;

import labyrinth.Labyrinth;

public interface IFacade {
    String generate(int width, int height);

    Labyrinth generate(int width, int height, long seed);
    void solve(String algo);

    void play();

    void save(String labyrinth, String filename);

}
