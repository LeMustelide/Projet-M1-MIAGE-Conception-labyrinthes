package labyrinth;

public interface IPlayerMovement {
    int getPlayerX();
    int getPlayerY();
    void setPlayerX(int x);
    void setPlayerY(int y);
    void movePlayer(int x, int y);
    void setPlayerPosition(int x, int y);
}
