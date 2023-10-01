package labyrinth;

import labyrinth.Labyrinth;

class LabyrinthSVGGenerator {

    public String generateSVG(Labyrinth labyrinth) {
        int cellSize = 20;  // Taille de chaque cellule en pixels
        StringBuilder svgContent = new StringBuilder();

        int width = (labyrinth.verticalWalls[0].length) * cellSize;
        int height = (labyrinth.horizontalWalls.length) * cellSize;

        svgContent.append(String.format("<svg width=\"%d\" height=\"%d\" xmlns=\"http://www.w3.org/2000/svg\">\n", width, height));
        svgContent.append("<style>"
                + ".entry{stroke:green;stroke-width:3;}"
                + ".exit{stroke:red;stroke-width:3;}"
                + "line{stroke:black;stroke-width:3;}"
                + "</style>\n");


        // Dessiner les murs verticaux
        for (int i = 0; i < labyrinth.verticalWalls.length; i++) {
            int y1 = i * cellSize + 5;
            for (int j = 0; j < labyrinth.verticalWalls[i].length; j++) {
                if (labyrinth.verticalWalls[i][j]) {
                    int x1 = j * cellSize + 5;
                    int x2 = x1;
                    int y2 = y1 + cellSize;
                    svgContent.append("<line x1=\"").append(x1).append("\" y1=\"").append(y1)
                            .append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\"/>\n");
                }
            }
        }

        // Dessiner les murs horizontaux
        for (int i = 0; i < labyrinth.horizontalWalls.length; i++) {
            int y1 = i * cellSize + 5;
            for (int j = 0; j < labyrinth.horizontalWalls[i].length; j++) {
                if (labyrinth.horizontalWalls[i][j]) {
                    int x1 = j * cellSize + 5;
                    int x2 = x1 + cellSize;
                    int y2 = y1;
                    svgContent.append("<line x1=\"").append(x1).append("\" y1=\"").append(y1)
                            .append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\"/>\n");
                }
            }
        }

        int x1 = 0 * cellSize + 5;
        int y1 = labyrinth.start * cellSize + 5;
        int x2 = x1;
        int y2 = y1 + cellSize;
        svgContent.append(String.format("<line class=\"entry\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" />\n", x1, y1, x2, y2));

        x1 = (labyrinth.horizontalWalls.length-1) * cellSize + 5;
        y1 = labyrinth.end * cellSize + 5;
        x2 = x1;
        y2 = y1 + cellSize;
        svgContent.append(String.format("<line class=\"exit\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" />\n", x1, y1, x2, y2));

        svgContent.append("</svg>");

        return svgContent.toString();
    }
}