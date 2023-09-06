import entities.Labyrinth;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Main {
    public static void main(String[] args) {
        LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        Labyrinth labyrinth = new Labyrinth(4, 5);
        labyrinth.generatePerfectLabyrinth();
        copyToClipboard(generator.generateSVG(labyrinth));
    }

    public static void copyToClipboard(String str) {
        StringSelection stringSelection = new StringSelection(str);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        //System.out.println(str);
    }

}