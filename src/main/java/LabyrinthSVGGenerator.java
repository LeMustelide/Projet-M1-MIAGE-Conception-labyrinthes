import entities.Labyrinth;

class LabyrinthSVGGenerator {

    public String generateSVG(Labyrinth labyrinth) {
        int cellSize = 20;  // Taille de chaque cellule en pixels
        StringBuilder svgContent = new StringBuilder();

        int width = (labyrinth.verticalWalls[0].length) * cellSize;
        int height = (labyrinth.horizontalWalls.length) * cellSize;

        svgContent.append("<svg width=\"" + width + "\" height=\"" + height + "\" xmlns=\"http://www.w3.org/2000/svg\">\n");

        // Dessiner les murs verticaux
        for (int i = 0; i < labyrinth.verticalWalls.length; i++) {
            for (int j = 0; j < labyrinth.verticalWalls[i].length + 1; j++) {
                if (labyrinth.verticalWalls[i][j]) {
                    int x1 = j * cellSize;
                    int y1 = i * cellSize;
                    int x2 = x1;
                    int y2 = y1 + cellSize;
                    svgContent.append("<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" stroke=\"black\" />\n");
                }
            }
        }

        // Dessiner les murs horizontaux
        for (int i = 0; i < labyrinth.horizontalWalls.length; i++) {
            for (int j = 0; j < labyrinth.horizontalWalls[i].length; j++) {
                if (labyrinth.horizontalWalls[i][j]) {
                    int x1 = j * cellSize;
                    int y1 = i * cellSize;
                    int x2 = x1 + cellSize;
                    int y2 = y1;
                    svgContent.append("<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" stroke=\"black\" />\n");
                }
            }
        }

        svgContent.append("</svg>");

        return svgContent.toString();
    }
}
