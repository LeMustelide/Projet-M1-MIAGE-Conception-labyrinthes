package labyrinth.generator;

import labyrinth.LabyrinthBase;
import labyrinth.LabyrinthFunctionLibrary;
import labyrinth.UnionFind;

import java.util.ArrayList;
import java.util.Random;

public class HexagonLabyrinthGenerator implements ILabyrinthGenerator{
    @Override
    public void generatePerfectLabyrinth(long seed, LabyrinthBase labyrinth) {
        LabyrinthFunctionLibrary.setRand(new Random(seed));

        int rows = labyrinth.getVerticalWalls().length;
        int cols = labyrinth.getVerticalWalls()[0].length;

        ArrayList<Integer> walls = new ArrayList<>();

        // Ajouter tous les murs possibles
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Murs verticaux
                if (y > 0 && y < cols - 1) {
                    walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, WallType.VERTICAL));
                }
                // Murs diagonaux montants et descendants
                if (y > 0 && y < rows - 1 && x > 0) {
                    walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, WallType.ASCENDING_DIAGONAL));
                    walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, WallType.DESCENDING_DIAGONAL));
                } else {
                    if (x%2 != 0 || (x > 0 && y == rows - 1 && x % 2 == 0)) {
                        walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, WallType.ASCENDING_DIAGONAL));
                    }
                    if (x % 2 == 0 && x > 0 || x == cols - 1 && y % 2 == 0 || y == 0 && x % 2 != 0) {
                        walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, WallType.DESCENDING_DIAGONAL));
                    }
                }
            }
        }

        walls = (ArrayList<Integer>) LabyrinthFunctionLibrary.shuffle(walls);
        System.out.println(walls.size());
        UnionFind uf = labyrinth.getUnionFind();
        for (int wall : walls) {
            int x = LabyrinthFunctionLibrary.decodeX(wall);
            int y = LabyrinthFunctionLibrary.decodeY(wall);
            WallType type = LabyrinthFunctionLibrary.decodeWallType(wall);

            int cell1Id, cell2Id;
            cell1Id = y * cols + x;
            cell2Id = y * cols + (x + 1);
            int cell1Parent = uf.find(cell1Id);
            int cell2Parent = uf.find(cell2Id);
            if (cell1Parent != cell2Parent) {
                uf.union(cell1Id, cell2Id);
                switch (type) {
                    case VERTICAL:
                        if(labyrinth.getVerticalWalls()[y][x])
                            labyrinth.updateVerticalWalls(y, x, false);
                        break;
                    case ASCENDING_DIAGONAL:
                        labyrinth.getAscendingDiagonalWalls()[y][x] = false;
                        break;
                    case DESCENDING_DIAGONAL:
                        labyrinth.getDescendingDiagonalWalls()[y][x] = false;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown wall type");
                }
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
        labyrinth.updateVerticalWalls(labyrinth.getEnd(), cols-1, false);
        System.out.println("start : " + labyrinth.getStart() + " end : " + labyrinth.getEnd());

    }
}
