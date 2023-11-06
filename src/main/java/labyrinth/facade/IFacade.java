package labyrinth.facade;

public interface IFacade {
    String generate(int width, int height);

    void generate(int width, int height, int seed);
    void solve(String labyrinth);
    void

}
