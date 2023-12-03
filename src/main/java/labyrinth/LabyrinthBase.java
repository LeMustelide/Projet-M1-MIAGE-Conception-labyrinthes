package labyrinth;

import labyrinth.generator.ILabyrinthGenerator;
import labyrinth.labyrinthCanvasGenerator.ILabyrinthCanvasGenerator;
import labyrinth.solver.ILabyrinthSolver;

import java.util.Random;

public abstract class LabyrinthBase {
    protected Random rand = new Random();
    protected boolean[][] verticalWalls;    // Murs verticaux: (m+1) x n
    protected boolean[][] horizontalWalls;  // Murs horizontaux: m x (n+1)
    protected boolean[][] ascendingDiagonalWalls; // Murs diagonaux ascendants: (m+1) x (n+1)
    protected boolean[][] descendingDiagonalWalls; // Murs diagonaux descendants: (m+1) x (n+1)

    protected int start, end;
    protected IPlayerMovement playerMovement;
    protected UnionFind uf;
    private ILabyrinthSolver solver;
    private ILabyrinthCanvasGenerator canvasGenerator;

    private ILabyrinthGenerator generator;


    public LabyrinthBase(int m, int n) {
        start = 0;
        end = 0;
        playerMovement = new PlayerMovement(this);
    }

    public void addVerticalWalls(int m, int n) {
        this.verticalWalls = new boolean[m][n+1];
    }

    public void addHorizontalWalls(int m, int n) {
        this.horizontalWalls = new boolean[m+1][n];
    }

    public void addAscendingDiagonalWalls(int m, int n) {
        this.ascendingDiagonalWalls = new boolean[m+1][n+1]; // revoire la taille
    }

    public void addDescendingDiagonalWalls(int m, int n) {
        this.descendingDiagonalWalls = new boolean[m+1][n+1]; // revoire la taille
    }

    public boolean isMovePossible2D(int newX, int newY) {
        int playerX = playerMovement.getPlayerX();
        int playerY = playerMovement.getPlayerY();
        // Vérifie si la nouvelle position est à l'intérieur des limites du labyrinthe
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

        if(newY == end && newX  == verticalWalls[0].length - 1){
            System.out.println("Victoire");
            return false; // TODO: victoire
        }

        return true;
    }

    public IPlayerMovement getPlayerMovement() {
        return playerMovement;
    }

    public void updateVerticalWalls(int m, int n, boolean value){
        this.verticalWalls[m][n] = value;
    }

    public void updateHorizontalWalls(int m, int n, boolean value){
        this.horizontalWalls[m][n] = value;
    }

    public UnionFind getUnionFind(){
        return this.uf;
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public boolean[][] getAscendingDiagonalWalls() {
        return ascendingDiagonalWalls;
    }

    public boolean[][] getDescendingDiagonalWalls() {
        return descendingDiagonalWalls;
    }

    public void setStart(int start){
        this.start = start;
    }

    public void setEnd(int end){
        this.end = end;
    }

    public int getStart(){
        return this.start;
    }

    public int getEnd(){
        return this.end;
    }

    public boolean isMovePossible(int x, int y, int newX, int newY) {
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

    public int[] getNextMove(int x, int y, int direction) {
        switch (direction) {
            case 0: return new int[]{x + 1, y}; // Est
            case 1: return new int[]{x, y + 1}; // Sud
            case 2: return new int[]{x - 1, y}; // Ouest
            case 3: return new int[]{x, y - 1}; // Nord
            default: throw new IllegalArgumentException("Direction non valide");
        }
    }

    public void setSolver(ILabyrinthSolver solver) {
        this.solver = solver;
    }

    public ILabyrinthSolver getSolver() {
        return solver;
    }

    public void setCanvasGenerator(ILabyrinthCanvasGenerator canvasGenerator) {
        this.canvasGenerator = canvasGenerator;
    }

    public ILabyrinthCanvasGenerator getCanvasGenerator() {
        return canvasGenerator;
    }

    public void setGenerator(ILabyrinthGenerator generator) {
        this.generator = generator;
    }

    public ILabyrinthGenerator getGenerator() {
        return generator;
    }
}
