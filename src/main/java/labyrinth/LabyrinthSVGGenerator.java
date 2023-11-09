package labyrinth;

import labyrinth.Labyrinth;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LabyrinthSVGGenerator {

    public String generateSVG(Labyrinth labyrinth) {
        int cellSize = 20;  // Taille de chaque cellule en pixels
        StringBuilder svgContent = new StringBuilder();
        boolean[][] hWalls = labyrinth.getHorizontalWalls();
        boolean[][] vWalls = labyrinth.getVerticalWalls();

        int width = (vWalls[0].length) * cellSize;
        int height = (hWalls.length) * cellSize;

        svgContent.append("<svg width=\"").append(width).append("\" height=\"").append(height).append("\" xmlns=\"http://www.w3.org/2000/svg\">\n");
        svgContent.append("<style>"
                + ".entry{stroke:green;stroke-width:3;}"
                + ".exit{stroke:red;stroke-width:3;}"
                + "line{stroke:black;stroke-width:3;}"
                + "</style>\n");


        // Dessiner les murs verticaux
        for (int i = 0; i < vWalls.length; i++) {
            int y1 = i * cellSize + 5;
            for (int j = 0; j < vWalls[i].length; j++) {
                if (vWalls[i][j]) {
                    int x1 = j * cellSize + 5;
                    int x2 = x1;
                    int y2 = y1 + cellSize;
                    svgContent.append("<line x1=\"").append(x1).append("\" y1=\"").append(y1)
                            .append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\" />\n");
                }
            }
        }
        // Dessiner les murs horizontaux
        for (int i = 0; i < hWalls.length; i++) {
            int y1 = i * cellSize + 5;
            for (int j = 0; j < hWalls[i].length; j++) {
                if (hWalls[i][j]) {
                    int x1 = j * cellSize + 5;
                    int x2 = x1 + cellSize;
                    int y2 = y1;
                    svgContent.append("<line x1=\"").append(x1).append("\" y1=\"").append(y1)
                            .append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\" />\n");
                }
            }
        }

        int x1 = 0 * cellSize + 5;
        int y1 = labyrinth.getEntry() * cellSize + 5;
        int x2 = x1;
        int y2 = y1 + cellSize;
        svgContent.append("<line class=\"entry\" x1=\"").append(x1).append("\" y1=\"").append(y1)
                .append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\"/>");

        x1 = (hWalls.length-1) * cellSize + 5;
        y1 = labyrinth.getExit() * cellSize + 5;
        x2 = x1;
        y2 = y1 + cellSize;
        svgContent.append("<line class=\"exit\" x1=\"").append(x1).append("\" y1=\"").append(y1)
                .append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\"/>");



        List<int[]> path = labyrinth.getPath();

        if(path != null) {

            // Convertissez ce chemin en une chaîne de points pour la balise <polyline>
            StringBuilder pathPoints = new StringBuilder();
            for (int[] point : path) {
                // Ici, je suppose que `point[0]` est x et `point[1]` est y
                int svgX = point[0] * cellSize + cellSize / 2 + 5; // Convertir la coordonnée de la grille en coordonnée SVG
                int svgY = point[1] * cellSize + cellSize / 2 + 5; // et centrer le chemin dans la cellule
                pathPoints.append(svgX).append(",").append(svgY).append(" ");
            }

            // Ajouter la balise <polyline> avec les points du chemin au contenu SVG
            svgContent.append("<polyline points=\"")
                    .append(pathPoints.toString())
                    .append("\" style=\"fill:none;stroke:red;stroke-width:2\" />\n");
        }


        svgContent.append("</svg>");

        return svgContent.toString();
    }
}
