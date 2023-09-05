import entities.Labyrinth;
public class Main {
    public static void main(String[] args) {
        LabyrinthSVGGenerator generator = new LabyrinthSVGGenerator();
        Labyrinth labyrinth = new Labyrinth(10, 10);
        labyrinth.generatePerfectLabyrinth();
        System.out.println(generator.generateSVG(labyrinth));
    }

}