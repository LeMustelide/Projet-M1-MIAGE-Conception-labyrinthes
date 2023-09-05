package entities;

class Cell {
    int id;       // Identifiant unique de la cellule
    int x, y;     // Coordonnées de la cellule dans la grille

    public Cell(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
