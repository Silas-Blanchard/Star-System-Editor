package com.sysedit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MainEditorControl {
    private Sim sim = Sim.getSim();

    @FXML
    private ScrollPane scroller;

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
        Group group = sim.get_the_group();

        StackPane stacky = new StackPane();
        stacky.getChildren().addAll(imageView, group);

        scroller.setContent(stacky);
    }

}
