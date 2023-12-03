package labyrinth.generator;

import labyrinth.LabyrinthBase;
import labyrinth.LabyrinthFunctionLibrary;
import labyrinth.UnionFind;

import java.util.ArrayList;
import java.util.Random;

public class SquareLabyrinthGenerator implements ILabyrinthGenerator{
    public void generatePerfectLabyrinth(long seed, LabyrinthBase labyrinth) {
        LabyrinthFunctionLibrary.setRand(new Random(seed));

        int line = labyrinth.getVerticalWalls().length;
        int col = labyrinth.getHorizontalWalls()[0].length;

        ArrayList<Integer> walls = new ArrayList<>();

        for (int y = 0; y < line; y++) {
            for (int x = 0; x < col; x++) {
                if (y < line - 1) {
                    walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, WallType.HORIZONTAL));
                }
                if (x < col - 1) {
                    walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, WallType.VERTICAL));
                }
            }
        }

        walls = (ArrayList<Integer>) LabyrinthFunctionLibrary.shuffle(walls);

        for (int wall : walls) {
            int x, y;
            WallType wallType;
            x = LabyrinthFunctionLibrary.decodeX(wall);
            y = LabyrinthFunctionLibrary.decodeY(wall);
            wallType = LabyrinthFunctionLibrary.decodeWallType(wall);

            int cell1Id, cell2Id;
            UnionFind uf = labyrinth.getUnionFind();
            int cell1Parent, cell2Parent;
            switch (wallType) {
                case HORIZONTAL:
                    cell1Id = (y * col + x);
                    cell2Id = ((y + 1) * col + x);
                    cell1Parent = uf.find(cell1Id);
                    cell2Parent = uf.find(cell2Id);
                    if (cell1Parent != cell2Parent && cell2Parent != -1 && labyrinth.getHorizontalWalls()[y+1][x]) {
                        labyrinth.updateHorizontalWalls(y+1, x, false);
                        uf.union(cell1Id, cell2Id);
                    }
                    break;
                case VERTICAL:
                    cell1Id = (y * col + x);
                    cell2Id = (y * col + (x + 1));
                    cell1Parent = uf.find(cell1Id);
                    cell2Parent = uf.find(cell2Id);
                    if (cell1Parent != cell2Parent && cell2Parent != -1 && labyrinth.getVerticalWalls()[y][x+1]) {
                        labyrinth.updateVerticalWalls(y, x+1, false);
                        uf.union(cell1Id, cell2Id);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown wall type");
            }

            if (uf.numSets == 1) {
                break;
            }
        }

        labyrinth.setStart(LabyrinthFunctionLibrary.rand.nextInt(0, labyrinth.getVerticalWalls().length));
        labyrinth.setEnd(LabyrinthFunctionLibrary.rand.nextInt(0, labyrinth.getVerticalWalls().length));

        labyrinth.getPlayerMovement().setPlayerX(0);
        labyrinth.getPlayerMovement().setPlayerY(labyrinth.getStart());
        labyrinth.updateVerticalWalls(labyrinth.getStart(), 0, false);
        labyrinth.updateVerticalWalls(labyrinth.getEnd(), labyrinth.getVerticalWalls()[0].length-1, false);
    }
}
