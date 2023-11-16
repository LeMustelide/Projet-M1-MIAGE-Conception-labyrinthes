package labyrinth;

import java.util.*;

public class Labyrinth {

    private Random rand = new Random();
    private boolean[][] verticalWalls;    // Murs verticaux: (m+1) x n
    private boolean[][] horizontalWalls;  // Murs horizontaux: m x (n+1)

    private int playerX = 0;
    private int playerY;
    UnionFind uf;

    private int start, end;

    private List<int[]> path;

    private int size;

    public Labyrinth(int m, int n) {
        this.verticalWalls = new boolean[m][n+1];
        this.horizontalWalls = new boolean[m+1][n];
        this.uf = new UnionFind(m * n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n+1; j++) {
                verticalWalls[i][j] = true;
            }
        }

        for (int i = 0; i < m+1; i++) {
            for (int j = 0; j < n; j++) {
                horizontalWalls[i][j] = true;
            }
        }
    }

    public void generatePerfectLabyrinth(long seed) {
        this.rand = new Random(seed);
        int line = verticalWalls.length;
        int col = horizontalWalls[0].length;

        ArrayList<Integer> walls = new ArrayList<>();

        for (int y = 0; y < line; y++) {
            for (int x = 0; x < col; x++) {
                if (y < line - 1) {
                    walls.add(encodeWall(x, y, true));
                }
                if (x < col - 1) {
                    walls.add(encodeWall(x, y, false));
                }
            }
        }

        shuffle(walls);

        for (int wall : walls) {
            int x, y;
            boolean isHorizontal;
            x = decodeX(wall);
            y = decodeY(wall);
            isHorizontal = decodeOrientation(wall);

            int cell1Id, cell2Id;
            if (isHorizontal) {
                cell1Id = (y * col + x);
                cell2Id = ((y + 1) * col + x);
                int cell1Parent = uf.find(cell1Id);
                int cell2Parent = uf.find(cell2Id);
                if (cell1Parent != cell2Parent && cell2Parent != -1 && horizontalWalls[y+1][x]) {
                    horizontalWalls[y+1][x] = false;
                    uf.union(cell1Id, cell2Id);
                }
            } else {
                cell1Id = (y * col + x);
                cell2Id = (y * col + (x + 1));
                int cell1Parent = uf.find(cell1Id);
                int cell2Parent = uf.find(cell2Id);
                if (cell1Parent != cell2Parent && cell2Parent != -1 && verticalWalls[y][x+1]) {
                    verticalWalls[y][x+1] = false;
                    uf.union(cell1Id, cell2Id);
                }
            }

            if (uf.numSets == 1) {
                break;
            }
        }

        start = rand.nextInt(col);
        end = rand.nextInt(col);

        playerX = 0;
        playerY = start;

        verticalWalls[start][0] = false;
        verticalWalls[end][line] = false;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    // Méthode pour déplacer le joueur
    public void movePlayer(int newX, int newY) {
        if (isMovePossible2D(newX, newY)) {
            this.playerX = newX;
            this.playerY = newY;
        }
    }


    private int encodeWall(int x, int y, boolean isHorizontal) {
        return (x << 1) | (y << 16) | (isHorizontal ? 1 : 0);
    }

    private int decodeX(int wall) {
        return (wall >> 1) & 0x7FFF;
    }

    private int decodeY(int wall) {
        return (wall >> 16) & 0x7FFF;
    }

    private boolean decodeOrientation(int wall) {
        return (wall & 1) == 1;
    }

    private void shuffle(List<?> list) {
        int size = list.size();
        for (int i = size - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Collections.swap(list, i, index);
        }
    }

    public List<int[]> solveWithRightHandAlgorithm() {
        path = new ArrayList<>(); // Pour stocker le chemin emprunté

        // Commencer à l'entrée du labyrinthe
        int x = 0, y = start;
        int direction = 0; // Représenter les directions avec des chiffres : 0 = Est, 1 = Sud, 2 = Ouest, 3 = Nord

        path.add(new int[] {x, y}); // Ajouter la position de départ au chemin

        while (true) {
            // Vérifier si nous sommes à la sortie
            if (x == horizontalWalls[0].length - 1 && y == end) {
                break;
            }

            boolean moved = false;

            // Essayez de tourner à droite et de bouger dans cette direction
            int newDirection = (direction + 3) % 4;
            int[] nextMove = getNextMove(x, y, newDirection);

            if (isMovePossible(x, y, nextMove[0], nextMove[1])) {
                direction = newDirection;
                x = nextMove[0];
                y = nextMove[1];
                path.add(new int[]{x, y});
                moved = true;
            } else {
                // Essayez de bouger tout droit si bouger à droite n'est pas possible
                nextMove = getNextMove(x, y, direction);
                if (isMovePossible(x, y, nextMove[0], nextMove[1])) {
                    x = nextMove[0];
                    y = nextMove[1];
                    path.add(new int[]{x, y});
                    moved = true;
                }
            }

            if (!moved) {
                // Si nous ne pouvons pas bouger à droite ou tout droit, alors nous tournons à gauche
                direction = (direction + 1) % 4;
            }
        }
        return path;
    }

    public List<int[]> solveWithDijkstraAlgorithm() {
        // Initialisation
        int rows = horizontalWalls.length;
        int cols = verticalWalls[0].length;
        int[][] distances = new int[rows][cols];
        int[][] prev = new int[rows][cols];
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> distances[a[0]][a[1]] - distances[b[0]][b[1]]);

        for (int[] row : distances) Arrays.fill(row, Integer.MAX_VALUE); // INFINITY
        for (int[] row : prev) Arrays.fill(row, -1); // Undefined

        // Source distance
        distances[0][start] = 0;
        queue.offer(new int[]{0, start});

        // Tant que la file d'attente n'est pas vide
        while (!queue.isEmpty()) {
            int[] u = queue.poll();
            int x = u[0], y = u[1];

            // Si c'est la sortie
            if (x == cols - 1 && y == end) break;

            // Vérifier les voisins (haut, bas, gauche, droite)
            for (int[] direction : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
                int newX = x + direction[0], newY = y + direction[1];
                if (newX >= 0 && newX < cols && newY >= 0 && newY < rows && isMovePossible(x, y, newX, newY)) {
                    int newDist = distances[x][y] + 1; // Coût de déplacement uniforme
                    if (newDist < distances[newX][newY]) {
                        distances[newX][newY] = newDist;
                        prev[newX][newY] = encodePosition(x, y); // Encode current position as previous
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
        }

        // Reconstruire le chemin
        path = new ArrayList<>();
        int[] tracePos = {cols - 1, end};
        while (tracePos != null) {
            path.add(0, tracePos); // Ajoute au début pour reconstruire le chemin
            tracePos = decodePosition(prev[tracePos[0]][tracePos[1]]); // Decode to get previous position
        }

        return path;
    }

    private int encodePosition(int x, int y) {
        return x * horizontalWalls[0].length + y; // Encode position as single integer
    }

    private int[] decodePosition(int pos) {
        if (pos == -1) return null;
        return new int[]{pos / horizontalWalls[0].length, pos % horizontalWalls[0].length}; // Decode position
    }

    private boolean isMovePossible(int x, int y, int newX, int newY) {
        // Vérifier les limites du labyrinthe
        if (newX < 0 || newY < 0 || newX >= verticalWalls[0].length || newY >= horizontalWalls.length) {
            return false; // Le mouvement est en dehors des limites du labyrinthe
        }

        // Déplacement vers l'est
        if (newX > x) {
            return !verticalWalls[y][x + 1]; // Il ne doit pas y avoir de mur à droite
        }

        // Déplacement vers l'ouest
        if (newX < x) {
            return !verticalWalls[y][x]; // Il ne doit pas y avoir de mur à gauche
        }

        // Déplacement vers le sud
        if (newY > y) {
            return !horizontalWalls[y + 1][x]; // Il ne doit pas y avoir de mur en bas
        }

        // Déplacement vers le nord
        if (newY < y) {
            return !horizontalWalls[y][x]; // Il ne doit pas y avoir de mur en haut
        }

        return false; // Par défaut, le mouvement n'est pas possible
    }

    public boolean isMovePossible2D(int newX, int newY) {
        // Vérifier si la nouvelle position est à l'intérieur des limites du labyrinthe
        if (newX < 0 || newY < 0 || newX >= verticalWalls[0].length || newY >= horizontalWalls.length) {
            return false;
        }

        // Déplacement vers l'est (droite)
        if (newX > playerX) {
            if (verticalWalls[playerY][playerX + 1]) {  // Vérifier s'il y a un mur à droite de la position actuelle
                return false;
            }
        }

        // Déplacement vers l'ouest (gauche)
        if (newX < playerX) {
            if (verticalWalls[playerY][playerX]) {  // Vérifier s'il y a un mur à gauche de la position actuelle
                return false;
            }
        }

        // Déplacement vers le sud (bas)
        if (newY > playerY) {
            if (horizontalWalls[playerY + 1][playerX]) {  // Vérifier s'il y a un mur en dessous de la position actuelle
                return false;
            }
        }

        // Déplacement vers le nord (haut)
        if (newY < playerY) {
            if (horizontalWalls[playerY][playerX]) {  // Vérifier s'il y a un mur au-dessus de la position actuelle
                return false;
            }
        }

        return true;
    }

    private int[] getNextMove(int x, int y, int direction) {
        switch (direction) {
            case 0: return new int[]{x + 1, y}; // Est
            case 1: return new int[]{x, y + 1}; // Sud
            case 2: return new int[]{x - 1, y}; // Ouest
            case 3: return new int[]{x, y - 1}; // Nord
            default: throw new IllegalArgumentException("Direction non valide");
        }
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public int getEntry() {
        return start;
    }

    public int getExit() {
        return end;
    }

    public List<int[]> getPath() {
        return path;
    }

    public void setPlayerPosition(int x, int y) {
        this.playerX = x;
        this.playerY = y;
    }

}
