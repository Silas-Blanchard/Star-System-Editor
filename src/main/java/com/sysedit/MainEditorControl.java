package com.sysedit;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

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
        
    }

    @FXML
    public void deselect_all(ActionEvent event) {

    }

    @FXML
    public void newParent(ActionEvent event) {
        sim.createNewSystem();
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

    public void setContent() throws IOException{
        InputStream background_stream = new FileInputStream("src/main/resources/com/sysedit/background.png");
        Image largeImage = new Image(background_stream);
        ImageView imageView = new ImageView(largeImage);

        scroller.setPannable(true);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);

        Sim sim = Sim.getSim();
        Group the_group = sim.get_the_group();
        Pane mainpane = new Pane(the_group);

        ContextMenu contextmenu = new ContextMenu();
        MenuItem paste = new MenuItem("Paste");
        MenuItem newSystemParent = new MenuItem("New System Parent");
        contextmenu.getItems().addAll(paste, newSystemParent);

        //newSystemParent.setOnAction(e-> sim.set_new_parent());

        mainpane.setOnContextMenuRequested(e->{
                contextmenu.show(imageView, e.getScreenX(), e.getScreenY());
            }
        );

        StackPane stacky = new StackPane();
        stacky.getChildren().addAll(imageView, mainpane);

        scroller.setContent(stacky);

        sim.startPoint = new Point2D(largeImage.getWidth() / 2, largeImage.getHeight() / 2);
        sim.translateTheGroup(sim.startPoint.getX(), sim.startPoint.getY());
    }
}