package labyrinth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LabyrinthGenerator {
    public void generatePerfectLabyrinth(long seed, LabyrinthBase labyrinth) {
        LabyrinthFunctionLibrary.setRand(new Random(seed));
        int line = labyrinth.verticalWalls.length;
        int col = labyrinth.horizontalWalls[0].length;

        ArrayList<Integer> walls = new ArrayList<>();

        for (int y = 0; y < line; y++) {
            for (int x = 0; x < col; x++) {
                if (y < line - 1) {
                    walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, true));
                }
                if (x < col - 1) {
                    walls.add(LabyrinthFunctionLibrary.encodeWall(x, y, false));
                }
            }
        }

        walls = (ArrayList<Integer>) LabyrinthFunctionLibrary.shuffle(walls);

        for (int wall : walls) {
            int x, y;
            boolean isHorizontal;
            x = LabyrinthFunctionLibrary.decodeX(wall);
            y = LabyrinthFunctionLibrary.decodeY(wall);
            isHorizontal = LabyrinthFunctionLibrary.decodeOrientation(wall);

            int cell1Id, cell2Id;
            UnionFind uf = labyrinth.getUnionFind();
            if (isHorizontal) {
                cell1Id = (y * col + x);
                cell2Id = ((y + 1) * col + x);
                int cell1Parent = uf.find(cell1Id);
                int cell2Parent = uf.find(cell2Id);
                if (cell1Parent != cell2Parent && cell2Parent != -1 && labyrinth.getHorizontalWalls()[y+1][x]) {
                    labyrinth.updateHorizontalWalls(y+1, x, false);
                    uf.union(cell1Id, cell2Id);
                }
            } else {
                cell1Id = (y * col + x);
                cell2Id = (y * col + (x + 1));
                int cell1Parent = uf.find(cell1Id);
                int cell2Parent = uf.find(cell2Id);
                if (cell1Parent != cell2Parent && cell2Parent != -1 && labyrinth.getVerticalWalls()[y][x+1]) {
                    labyrinth.updateVerticalWalls(y, x+1, false);
                    uf.union(cell1Id, cell2Id);
                }
            }

            if (uf.numSets == 1) {
                break;
            }
        }

        labyrinth.setStart(LabyrinthFunctionLibrary.rand.nextInt(0, labyrinth.getVerticalWalls().length));
        labyrinth.setEnd(LabyrinthFunctionLibrary.rand.nextInt(0, labyrinth.getVerticalWalls().length));

        labyrinth.getPlayerMovement().setPlayerX(0);
        labyrinth.getPlayerMovement().setPlayerY(labyrinth.start);
        labyrinth.updateVerticalWalls(labyrinth.start, 0, false);
        labyrinth.updateVerticalWalls(labyrinth.end, labyrinth.getVerticalWalls()[0].length-1, false);
    }
}
