package com.sysedit;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class Skin {
    //adding a skin to each Feature
    Feature reference;
    ImageView appearance;
    Image fullImage;
    Sim sim = Sim.getSim();
    StackPane form = new StackPane();
    double appearanceX;
    double appearanceY;
    double mouseX;
    double mouseY;
    
    public Skin(Feature ref){
        reference = ref;
    }

    public void pickImage() throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        File file = fileChooser.showOpenDialog(sim.window);

        if (file == null) {
            throw new Exception("No image selected");
        }

        fullImage = new Image(file.toURI().toString());
        appearance = new ImageView(fullImage);
        appearance.setPreserveRatio(true);

        // Set initial dimensions
        appearance.setFitWidth(200);
        appearance.setFitHeight(200);

        form.getChildren().add(appearance);
    }

    private void imbuePositioning(Boolean b){
        //makes the current appearance draggable
        if(b){
            appearance.setOnMousePressed(e -> {
                mouseX = e.getSceneX() - form.getLayoutX();
                mouseY = e.getSceneY() - form.getLayoutY();
                e.consume();
            });
    
            appearance.setOnMouseDragged(e -> {
                form.setLayoutX(e.getSceneX() - mouseX);
                form.setLayoutY(e.getSceneY() - mouseY);
                e.consume();
            });
            imbueResizing();
        }else{
            appearance.setOnMousePressed(null);
            appearance.setOnMouseDragged(null);
        }
    }

    private void imbueResizing(){
        final double handleSize = 10;

        // Create the resize handle and position it at the bottom-right
        Rectangle resizeHandle = new Rectangle(handleSize, handleSize);
        resizeHandle.setFill(Color.GRAY);
        form.getChildren().add(resizeHandle);
        StackPane.setAlignment(resizeHandle, javafx.geometry.Pos.BOTTOM_RIGHT);

        // Add dragging functionality for resize handle
        resizeHandle.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        resizeHandle.setOnMouseDragged(e -> {
            double deltaX = e.getSceneX() - mouseX;
            double deltaY = e.getSceneY() - mouseY;
            double newWidth = appearance.getFitWidth() + deltaX;
            double newHeight = appearance.getFitHeight() + deltaY;

            if (newWidth > handleSize && newHeight > handleSize) {
                appearance.setFitWidth(newWidth);
                appearance.setFitHeight(newHeight);
            }

            mouseX = e.getSceneX();
            mouseY = e.getSceneY();

            e.consume();
        });
    }

    public void cropImage() throws Exception{
        //imports the image, lets user position it, crops it, and then hands it to the feature to be added to its form
        sim.add_node(form);
        pickImage();
        imbuePositioning(true);
    }
}
