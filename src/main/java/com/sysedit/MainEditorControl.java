package com.sysedit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainEditorControl {
    private Sim sim = Sim.getSim();

    @FXML
    private ScrollPane scroller;

    @FXML
    private StackPane stacker;

    @FXML
    private MenuItem newButton;

    @FXML
    private MenuItem saveButton;

    @FXML
    private MenuItem closeButton;
  
    @FXML
    private MenuItem deselectButton;

    @FXML
    private MenuItem newParentButton;

    @FXML
    private MenuItem redo_button;

    @FXML
    private MenuItem selectAllButton;

    @FXML
    private MenuItem undo_button;

    @FXML
    public void closeApp(ActionEvent e){
        javafx.application.Platform.exit();
    }

    @FXML
    public void createNewSystem(ActionEvent e){
        sim.set_new_parent();
    }

    @FXML
    public void deselect_all(ActionEvent event) {
        Feature pie = sim.getSystemParent();
        Point2D pi = pie.getObjectivePoint();
        
        System.out.println(sim.getSystemParent().getObjectivePoint());
        System.out.println(sim.getSystemParent().form.getLayoutX() + " Layout X");
        System.out.println(sim.getSystemParent().form.getLayoutY() + " Layout Y");
    }

    @FXML
    public void newParent(ActionEvent event) {
        sim.set_new_parent();
    }

    @FXML
    public void redo(ActionEvent event) {

    }

    @FXML
    public void select_all(ActionEvent event) {

    }

    @FXML
    public void undo(ActionEvent event) {

    }

    @FXML
    public void saveAs(ActionEvent e){
        sim.saveAs();
    }

    public ScrollPane getScrollPane(){
        return scroller;
    }

    public void setContent() throws FileNotFoundException{
        InputStream background_stream = new FileInputStream("src/main/resources/com/sysedit/background.png");
        Image largeImage = new Image(background_stream);
        ImageView imageView = new ImageView(largeImage);

        scroller.setPannable(true);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);

        Sim sim = Sim.getSim();
        Group the_group = sim.get_the_group();
        Group special_group = sim.get_special_group();
        Pane mainpane = new Pane(the_group, special_group);

        StackPane stacky = new StackPane();
        stacky.getChildren().addAll(imageView, mainpane);

        scroller.setContent(stacky);

        sim.startPoint = new Point2D(largeImage.getWidth() / 2, largeImage.getHeight() / 2);
    }
}