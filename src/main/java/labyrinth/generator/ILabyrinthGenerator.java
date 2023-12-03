package labyrinth.generator;

import labyrinth.LabyrinthBase;

public interface ILabyrinthGenerator {
    void generatePerfectLabyrinth(long seed, LabyrinthBase labyrinth);
}
