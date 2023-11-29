package labyrinth;

public class PlayerMovement implements IPlayerMovement{
    private LabyrinthBase labyrinth;

    private int playerX, playerY;

    public PlayerMovement(LabyrinthBase labyrinth) {
        this.labyrinth = labyrinth;
    }

    @Override
    public int getPlayerX() {
        return playerX;
    }

    @Override
    public int getPlayerY() {
        return playerY;
    }

    @Override
    public void setPlayerX(int x) {
        this.playerX = x;
    }

    @Override
    public void setPlayerY(int y) {
        this.playerY = y;
    }

    @Override
    public void movePlayer(int x, int y) {
        if (labyrinth.isMovePossible2D(x, y)) {
            this.playerX = x;
            this.playerY = y;
        }
    }

    @Override
    public void setPlayerPosition(int x, int y) {
        this.playerX = x;
        this.playerY = y;
    }
}
