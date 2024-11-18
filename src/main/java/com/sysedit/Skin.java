package com.sysedit;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class Skin {
    //adding a skin to each Feature
    Feature reference;
    ImageView appearance;
    ImageView imageSkin;
    Image fullImage;
    Sim sim = Sim.getSim();
    StackPane form = new StackPane();
    double appearanceX;
    double appearanceY;
    double mouseX;
    double mouseY;

    double initialRotation;

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

    private void imbueRotating(){
        final double handleSize = 10;
        Rectangle rotateHandle = new Rectangle(handleSize, handleSize);
        rotateHandle.setFill(Color.GRAY);
        form.getChildren().add(rotateHandle);
        StackPane.setAlignment(rotateHandle, javafx.geometry.Pos.BOTTOM_CENTER);

        //rotating functionality
        rotateHandle.setOnMousePressed(e ->{
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();

            initialRotation = form.getRotate();
        });

        rotateHandle.setOnMouseDragged(e->{
            double oldAngle = appearance.getRotate();
            double centerX = form.getWidth() / 2;
            double centerY = form.getHeight() / 2;

            Point2D sceneCenter = form.localToScene(centerX, centerY);

            
            double deltaX = e.getSceneX() - sceneCenter.getX();
            double deltaY = e.getSceneY() - sceneCenter.getY();
            double newAngle = Math.atan2(deltaY, deltaX);
            form.setRotate(Math.toDegrees(newAngle));
            
            e.consume();
        });
    }

    public void cropImage() throws Exception{
        //imports the image, lets user position it, crops it, and then hands it to the feature to be added to its form
        sim.add_node(form);
        pickImage();
        imbueRotating();
        imbuePositioning(true);

        appearance.setOnMouseClicked(e->{
            if (e.getClickCount() == 2) {
                Circle intersectr = new Circle(reference.highlightRegion.getRadius());
                Point2D loc = reference.getTranslation();
                double circleX = loc.getX() + reference.getShapeOffset().getX();
                double circleY = loc.getY() + reference.getShapeOffset().getY();
                Point2D objectiveCenter = sim.get_the_group().localToScene(new Point2D(circleX, circleY));
                intersectr.setCenterX(objectiveCenter.getX());
                intersectr.setCenterY(objectiveCenter.getY());
                intersectr.setFill(Color.TRANSPARENT);

                // System.out.println("Circle center: " + intersectr.getCenterX() + ", " + intersectr.getCenterY());
                // System.out.println("ImageView bounds: " + form.getBoundsInParent());
            
                // Set intersectr as the clip directly
                appearance.setClip(intersectr);
            
                Bounds imageViewBounds = form.getBoundsInParent();
                Bounds circleBounds = intersectr.getBoundsInParent();

                //System.out.println("ImageViewBounds: " + form.getParent() + " circle parent " + intersectr.getParent());

                System.out.println("Circle bounds: " + circleBounds);
                System.out.println("ImageView bounds: " + imageViewBounds);

                // System.out.println("ImageView parent: " + appearance.getParent());
            
                if (imageViewBounds.intersects(circleBounds)) {
                    SnapshotParameters params = new SnapshotParameters();
                    params.setFill(Color.TRANSPARENT); // Transparent fill outside the cropped area
            
                    // Define viewport based on intersection of image and circle bounds
                    Rectangle2D viewport = new Rectangle2D(
                        circleBounds.getMinX() - imageViewBounds.getMinX(),
                        circleBounds.getMinY() - imageViewBounds.getMinY(),
                        circleBounds.getWidth(),
                        circleBounds.getHeight()
                    );
                    params.setViewport(viewport);
                
                    System.out.println("Circle center: " + intersectr.getCenterX() + ", " + intersectr.getCenterY());
                    System.out.println("ImageView bounds: " + appearance.getBoundsInParent());
                    // Take snapshot of the clipped area
                    WritableImage croppedImage = appearance.snapshot(params, null);
            
                    if (croppedImage != null) {
                        // Save the image
                        File file = new File("/home/silas/Desktop/Star-System-Editor/src/main/resources/image.png");
                        try {
                            ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, null), "png", file);
                            System.out.println("Image saved as: " + file.getAbsolutePath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            System.out.println("Failed to save the image.");
                        }
            
                        // Replace the current appearance with the cropped image
                        ImageView croppedImageView = new ImageView(croppedImage);
                        reference.setSkin(croppedImageView);
            
                        // Clean up
                        appearance.setClip(null);
                    } else {
                        System.out.println("Snapshot failed to produce an image.");
                    }
                }
            }
                // SnapshotParameters parameters = new SnapshotParameters();
                // parameters.setFill(Color.TRANSPARENT);
                // WritableImage intersectionImage = appearance.snapshot(parameters, null);

                // imageSkin = new ImageView(intersectionImage);
                // imageSkin.setViewOrder(-10.0);

                //making a new imageview from the cropped section               
        });
    }
}
