package labyrinth.view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoadLabyrinthImage {

    public void setSVGImage(String svgFilePath, ImageView labyrinth) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                // Charger le contenu SVG dans une chaîne
                String svgContent = Files.readString(Path.of(svgFilePath));

                // Créer un objet TranscoderInput avec le contenu SVG
                TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svgContent.getBytes()));

                // Créer un flux de sortie pour recevoir le résultat du transcodage
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                TranscoderOutput output = new TranscoderOutput(outputStream);

                // Créer un transcodeur PNG et transcoder le SVG
                PNGTranscoder transcoder = new PNGTranscoder();
                transcoder.transcode(input, output);

                // Fermer le flux de sortie
                outputStream.flush();
                outputStream.close();

                // Convertir le flux de sortie en un tableau d'octets et ensuite en une image JavaFX
                InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
                return new Image(in);
            }
        };

        loadImageTask.setOnSucceeded(e -> labyrinth.setImage(loadImageTask.getValue()));
        loadImageTask.setOnFailed(e -> {/* Gérer l'échec */});

        // Exécuter le Task dans un thread séparé
        new Thread(loadImageTask).start();
    }
}
