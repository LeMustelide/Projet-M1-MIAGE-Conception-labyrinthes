package labyrinth;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LabyrinthFunctionLibrary {
    public static Random rand;

    public static void setRand(Random r) {
        rand = r;
    }

    public static Random getRand() {
        return rand;
    }

    public static int decodeX(int wall) {
        return (wall >> 1) & 0x7FFF;
    }

    public static int decodeY(int wall) {
        return (wall >> 16) & 0x7FFF;
    }

    public static boolean decodeOrientation(int wall) {
        return (wall & 1) == 1;
    }

    public static List<?> shuffle(List<?> list) {
        int size = list.size();
        for (int i = size - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Collections.swap(list, i, index);
        }
        return list;
    }

    public static int encodeWall(int x, int y, boolean isHorizontal) {
        return (x << 1) | (y << 16) | (isHorizontal ? 1 : 0);
    }

    public static int encodePosition(int x, int y, boolean[][] horizontalWalls) {
        return x * horizontalWalls[0].length + y; // Encode position as single integer
    }

    public static int[] decodePosition(int pos, boolean[][] horizontalWalls) {
        if (pos == -1) return null;
        return new int[]{pos / horizontalWalls[0].length, pos % horizontalWalls[0].length}; // Decode position
    }
}
