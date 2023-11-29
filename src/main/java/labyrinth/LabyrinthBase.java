package labyrinth;

import java.util.Random;

public abstract class LabyrinthBase {
    protected Random rand = new Random();
    protected boolean[][] verticalWalls;    // Murs verticaux: (m+1) x n
    protected boolean[][] horizontalWalls;  // Murs horizontaux: m x (n+1)
    protected int start, end;
    protected IPlayerMovement playerMovement;
    protected UnionFind uf;


    public LabyrinthBase(int m, int n) {
        horizontalWalls = new boolean[m+1][n];
        start = 0;
        end = 0;
        playerMovement = new PlayerMovement(this);
    }

    public void addVerticalWalls(int m, int n) {
        this.verticalWalls = new boolean[m][n+1];
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
}
