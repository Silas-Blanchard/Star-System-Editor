package com.sysedit;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//helper class that stores that is currently selected and what needs to be rendered

public class Sim {
    private static Sim the_only_sim = null;
    private Stage window;
    private Group selection;
    private Group the_group = new Group(); //All shapes are stored in this.

    private Feature copied;
    private Feature system_parent;

    public ArrayList<Feature> features = new ArrayList<Feature>();

    private String title = "untitled";
    private javafx.scene.control.ScrollPane pane_thats_saved;

    public Point2D startPoint;

    public Point2D origin = new Point2D(0, 0);

    public ArrayList<Connector> connectors = new ArrayList<Connector>();

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
        // ObservableList<Node> h = the_group.getChildren();
        // System.out.println(title);
        //System.out.println(system_parent.system.get_features());
    }

    public void remove_node(Node node){
        this.the_group.getChildren().remove(node);
    }

    public Group get_the_group(){
        return the_group;
    }

    public void createNewSystem(){
        system_parent = createFeature();
        system_parent.setPrimaryVisiblity(true);

        Feature sat = createFeature();

        assignSatellite(system_parent, sat);

        render();

        Orbit o = sat.getSatelliteFormOrbit();
        o.apogee = 200.0;
        o.perigee = 200.0;

        render();

        Feature sat2 = createFeature();

        assignSatellite(system_parent, sat2);

        render();

        liberateSatellite(sat);

        sat.name = "pie";

        render();

        Feature sat12 = createFeature();

        assignSatellite(sat, sat12);

        render();

        liberateSatellite(sat12);

        render();
    }
    //longitude of the ascending node is how "rotated" the orbit is
    //mean anomaly is how far along the revolution the body is.  the mean anomaly is the fraction of an elliptical orbit's period that has elapsed since the orbiting body passed periapsis, expressed as an angle

    //     if (system_parent == null){
    //         system_parent = new World();
    //         system_parent.show_orbit = false;
    //         system_parent.is_expanded = true;
    //         system_parent.orbit.perigee = 0.0;
    //         system_parent.orbit.apogee = 0.0;
    //         system_parent.name = "Center";
    //         system_parent.setObjectivePoint(origin);
    //         system_parent.imbuePositioning(true);
    //     }
    //     else{
    //         World new_parent = new World();
    //         new_parent.orbit.perigee = 0.0;
    //         new_parent.orbit.apogee = 0.0;
    //         new_parent.show_orbit = false;
    //         system_parent.setParent(new_parent);
    //         system_parent = new_parent;
    //         system_parent.name = "Center";
    //         system_parent.setObjectivePoint(startPoint);
    //     }
    //     updateScene();

    public void render(){
        for(Feature f: features){
            f.render();
        }
    }

    public void addFeature(Feature f){
        the_group.getChildren().add(f.getForm());
        features.add(f);
    }

    public void saveAs(){
        Saver s = new Saver();
        s.saveAs(window, pane_thats_saved);
    }

    public void set_scrollpane(ScrollPane pane){
        this.pane_thats_saved = pane;
    }

    public ScrollPane get_scrollpane(){
        return this.pane_thats_saved;
    }

    public void set_title(String t){
        title = t;
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

    public void set_copy(Feature f){
        copied = f;
    }

    // public void paste(){
    //     // if(this.copied.parent == null){
    //     //     copied.setParent(system_parent);
    //     // }
    //     setSatellite(system_parent, copied);
    //     add_node(this.copied.form);
    // }

    // public void updateScene(){
    //     system_parent.render(); //recursive so it will render everything (
    //     // Circle centerMark = new Circle(0.0, 0.0, 1.0);
    //     // centerMark.setFill(Color.RED);
    //     // add_node(centerMark);
    // }

    public void assignSatellite(Feature host, Feature sat){
        if(sat.parent != null){
            removeSatellite(sat.parent, sat);
        }
        //Lets everyone know their role, the host keeps track of its new sat, and sat knows about its host
        host.addSatellite(sat);
        sat.setParent(host);
        sat.showPrimaryForm = false;
        sat.showSatelliteForm = true;
    }

    public void removeSatellite(Feature host, Feature sat){
        host.removeSatellite(sat);
        sat.setParent(null);
    }

    public Feature createFeature(){
        //adds feature's form to the scene and to the list
        Feature f = new Feature();
        features.add(f);
        the_group.getChildren().addAll(f.getPrimaryForm());
        return f;
    }

    public void deleteFeature(Feature toBeDeleted){
        for(Feature f: toBeDeleted.children){
            deleteFeature(f);
        }
        features.remove(toBeDeleted);
        
        if(toBeDeleted.parent != null){ //a feature's form is either stored in its parents group or in the feature
            toBeDeleted.parent.removeSatellite(toBeDeleted);
            toBeDeleted.parent.form.getChildren().remove(toBeDeleted.form);
        }else{
            the_group.getChildren().remove(toBeDeleted.getForm());
        }

        if(the_group.getChildren().contains(toBeDeleted.connectorIn.line)){
            the_group.getChildren().remove(toBeDeleted.connectorIn.line);
        }
    }

    public void liberateSatellite(Feature toBeLiberated){
        toBeLiberated.setPrimaryVisiblity(true);
        toBeLiberated.setSatelliteVisiblity(false);
        toBeLiberated.showConnector = true;
        the_group.getChildren().add(toBeLiberated.connectorIn.line);
    }

    // public void setSatellite(Feature host, Feature sat){
    //     //adds an existing feature to the system
    //     sat.show_name = true;
    //     sat.show_orbit = true;
    //     sat.setParent(host);

    //     sat.imbuePositioning(false);

    //     host.render();
    // }

    // public void addNewConnector(Connector c){
    //     connectors.add(c);
    // }

    // public Feature getSystemParent(){
    //     return system_parent;
    // }

    public void translateTheGroup(double x, double y){
        the_group.setLayoutX(x);
        the_group.setLayoutY(y);
    }
}
