package com.sysedit;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.ScrollPane;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//helper class that stores that is currently selected and what needs to be rendered

public class Sim {
    private static Sim the_only_sim = null;
    private Stage window;
    private ArrayList<Node> todo_list;
    private Group selection;
    private Group the_group = new Group(); //Note to self all shapes stored in this one. 
    private Group copied;
    private World system_parent;
    private String title = "untitled";
    private javafx.scene.control.ScrollPane pane_thats_saved;

    private Sim(){
        todo_list = new ArrayList<Node>();
    }

    public static Sim getSim(){
        if (the_only_sim == null){
            the_only_sim = new Sim();
        }
        return the_only_sim;
    }

    public void set_window(Stage window){
        this.window = window;
    }

    public void add_to_selection(Node node){
        if (this.selection == null){
            this.selection = new Group();
            //TODO make sure this adds a draggable handle to the group
        }
        this.selection.getChildren().add(node);
    }

    public void clear_selection(){
        this.selection.getChildren().removeAll();

    }

    public void add_node(Node node){
        this.the_group.getChildren().add(node);
    }

    public Group get_the_group(){
        return the_group;
    }

    public void set_new_parent(){
        system_parent = new World();
        World new_parent = new World();
        new_parent.orbit.perigee = 0.0;
        new_parent.orbit.apogee = 0.0;
        new_parent.show_orbit = false;

        if (system_parent == null){
            system_parent.parent = new_parent;
        }
        else{
            system_parent.parent = new_parent;
            system_parent = new_parent;
        }
        add_node(system_parent.render());
        updateScene();
    }

    public void set_scrollpane(ScrollPane pane){
        
    }

    public void saveAs(){
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

    public void set_title(String t){
        title = t;
    }

    public void set_scrollpane(javafx.scene.control.ScrollPane scrollPane) {
        this.pane_thats_saved = scrollPane;
    }

    public void open_editor(Feature feature, Boolean isError) throws IOException{
        Stage edit_window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
        VBox content = loader.load();
        if(isError){
            Text warning = new Text("Format Numbers correctly. \nNumber characters and a decimal only!");
            warning.setFill(Color.RED);
            content.getChildren().add(warning);
        }

        EditorController controller = loader.getController();
        controller.set_values(feature);

        Scene scene = new Scene(content);
        edit_window.setScene(scene);
        edit_window.show();
    }

    public void set_copy(Group g){
        copied = g;
    }

    public void paste(){
        add_node(copied);
    }

    public void updateScene(){
        the_group.getChildren().clear();
        add_node(system_parent.render());
    }

    public void create_satellite(Feature host){
        World new_world = new World();
        new_world.orbit.perigee = 100.0;
        new_world.orbit.apogee = 100.0;
        new_world.radius = 10.0;
        new_world.show_orbit = true;
        new_world.parent = host;
        host.system.add_feature(new_world);
        updateScene();
    }
}
