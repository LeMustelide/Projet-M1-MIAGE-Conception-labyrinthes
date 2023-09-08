package labyrinth;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        Labyrinth labyrinth = new Labyrinth(10, 10);
        labyrinth.generatePerfectLabyrinth();
        String svg = generator.generateSVG(labyrinth);

        try {
            File myObj = new File("labyrinthe.svg");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());

            } else {
                System.out.println("File already exists.");
            }
            FileOutputStream fos = new FileOutputStream("labyrinthe.svg");
            fos.write(svg.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}