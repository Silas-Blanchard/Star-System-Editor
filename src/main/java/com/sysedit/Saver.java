package com.sysedit;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Saver {
    public void saveAs(Stage window, javafx.scene.control.ScrollPane pane_thats_saved){
        Scene scene = window.getScene();
        int width = (int) scene.getWidth();
        int height = (int) scene.getHeight();

        // Get the content node inside the ScrollPane
        Node contentNode = pane_thats_saved.getContent();

        // Create a WritableImage with the size of the content node
        WritableImage writableImage = new WritableImage((int) contentNode.getBoundsInLocal().getWidth(),
                                                        (int) contentNode.getBoundsInLocal().getHeight());

        // Snapshot the content node into the WritableImage
        SnapshotParameters params = new SnapshotParameters();
        contentNode.snapshot(params, writableImage);

        // Use a FileChooser to get the file from the user
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save ScrollPane Contents as PNG");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        File file = fileChooser.showSaveDialog(pane_thats_saved.getScene().getWindow());

        if (file != null) {
            try {
                RenderedImage renderedImage = convertWritableImageToRenderedImage(writableImage);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                System.err.println("Error saving image: " + ex.getMessage());
            }
        }
    }

    private static RenderedImage convertWritableImageToRenderedImage(WritableImage writableImage) {
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        PixelReader pixelReader = writableImage.getPixelReader();
        //WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixelReader.getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }

        return bufferedImage;
    }
}
