package labyrinth;

import labyrinth.generator.WallType;

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
        return (wall >> 20) & 0x3FF; // Décale de 20 bits et masque avec 0x3FF pour obtenir les 10 bits de X
    }

    public static int decodeY(int wall) {
        return (wall >> 10) & 0x3FF; // Décale de 10 bits et masque avec 0x3FF pour obten
    }

    public static boolean decodeOrientation(int wall) {
        return (wall & 1) == 1;
    }

    public static WallType decodeWallType(int wall) {
        int typeBits = wall & 3;
        switch (typeBits) {
            case 0:
                return WallType.VERTICAL;
            case 1:
                return WallType.HORIZONTAL;
            case 2:
                return WallType.ASCENDING_DIAGONAL;
            case 3:
                return WallType.DESCENDING_DIAGONAL;
            default:
                throw new IllegalArgumentException("Unknown wall type");
        }
    }

    public static List<?> shuffle(List<?> list) {
        int size = list.size();
        for (int i = size - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Collections.swap(list, i, index);
        }
        return list;
    }

    public static int encodeWall(int x, int y, WallType wallType) {
        int typeBits;
        switch (wallType) {
            case VERTICAL:
                typeBits = 0; // 00 en binaire
                break;
            case HORIZONTAL:
                typeBits = 1; // 01 en binaire
                break;
            case ASCENDING_DIAGONAL:
                typeBits = 2; // 10 en binaire
                break;
            case DESCENDING_DIAGONAL:
                typeBits = 3; // 11 en binaire
                break;
            default:
                throw new IllegalArgumentException("Unknown wall type");
        }
        return (x << 20) | (y << 10) | typeBits;
    }

    public static int encodePosition(int x, int y, boolean[][] horizontalWalls) {
        int size = Math.max(horizontalWalls.length, horizontalWalls[0].length);
        return x * size + y; // Encode position as single integer
    }

    public static int[] decodePosition(int pos, boolean[][] horizontalWalls) {
        int size = Math.max(horizontalWalls.length, horizontalWalls[0].length);
        if (pos == -1) return null;
        return new int[]{pos / size, pos % size}; // Decode position
    }
}
