package com.sysedit;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Feature{
    public Feature parent;
    public Orbit orbit;
    public StarSystem system;

    public boolean is_expanded = false;
    public boolean show_orbit = true;
    public boolean show_name = true;
    public boolean showSatelliteForm = true;
    public boolean showPrimaryForm = false;
    public boolean showConnector = false;
    public boolean isAltForm = false;

    private int shininess;

    public String name = "Unnamed";

    public Double apogee = 100.0;
    public Double perigee = 100.0;
    public Double radius = 10.0;
    public Double angle = 90.0;
    public Double inclination = 0.0;

    private ContextMenu contextmenu;
    private Rightclickcontrol controller;

    public Group form = new Group(); //this is the form the feature takes.

    public Point2D shapeOffset = new Point2D(0, 0);

    public Point2D objectivePoint;

    public Connector connectorIn;
    public Group altform = new Group();

    public PrimaryBody primary;
    public SatelliteBody satellite;

    public ArrayList<Feature> children;
    public ArrayList<Ring> belts;



    Sim sim = Sim.getSim();

    public Feature(){
        primary = new PrimaryBody(this);
        satellite = new SatelliteBody(this);
        children = new ArrayList<Feature>();
        belts = new ArrayList<Ring>();
        connectorIn = new Connector(this);
        form.getChildren().add(altform);

        setLabelParams(name, 20);

        Line line1 = new Line(-5,0,5,0);
        Line line2 = new Line(0,-5,0,5);
        form.getChildren().addAll(line1, line2);
        //primary.form.getChildren().add(connectorIn.line);

        // try{
        //     FXMLLoader loader = new FXMLLoader(getClass().getResource("rightclickcontrol.fxml"));
        //     contextmenu = loader.load();
        //     controller = loader.getController();
        // } catch (IOException ex){
        //     System.out.println("Exception catch placeholder");
        // }
        // contextmenu = new ContextMenu();
        // MenuItem editSystem = new MenuItem("Edit System");
        // MenuItem addSatellite = new MenuItem("Add Satellite");
        // MenuItem addRing = new MenuItem("Add Ring");
        // MenuItem addMega = new MenuItem("Add Mega");
        // MenuItem addRingSys = new MenuItem("Add Ring System");
        // MenuItem toggleOrbit = new MenuItem("Toggle Orbit");
        // MenuItem toggleSystem = new MenuItem("Toggle System");
        // MenuItem changeParent = new MenuItem("Change Parent");
        // MenuItem copy = new MenuItem("Copy");
        // MenuItem copyGroup = new MenuItem("Copy Group");
        // MenuItem delete = new MenuItem("Delete");

        // editSystem.setOnAction(e-> {
        //     try {
        //         sim.open_editor(this, false);
        //     } catch (IOException e1) {
        //         // TODO Auto-generated catch block
        //         e1.printStackTrace();
        //     }
        // });
        // //addSatellite.setOnAction(e-> sim.create_satellite(this));

        // delete.setOnAction(e-> deleteFeature());

        // toggleOrbit.setOnAction(e-> show_orbit = !show_orbit);

        // toggleSystem.setOnAction(e-> {      
        //     is_expanded = !is_expanded;
        //     if(is_expanded){
        //         liberate();
        //     }});

        // contextmenu.getItems().addAll(editSystem, addSatellite, addRing, addMega, addRingSys, toggleOrbit, toggleSystem, changeParent, copy, copyGroup, delete);
    }

    public Group getForm(){
        return this.form;
    }

    public Group getPrimaryForm(){
        return primary.getForm();
    }

    public Group getSatelliteForm(){
        return satellite.getForm();
    }

    public void render(){
        if(showSatelliteForm){
            satellite.render();
        }

        if(showPrimaryForm){
            primary.render();
        }
    }

    public void addSatellite(Feature f){
        this.children.add(f);
        this.primary.form.getChildren().add(f.getSatelliteForm());
    }

    public void removeSatellite(Feature f){
        this.children.remove(f);
        this.primary.form.getChildren().remove(f.getSatelliteForm());
    }

    public void setParent(Feature f){
        parent = f;
        primary.nameLabel.setVisible(false);
        satellite.satelliteNameLabel.setVisible(true);
    }

    public void liberate(){//gotta be called or it doesn't know it's liberated
        setPrimaryVisiblity(true);
        setSatelliteVisiblity(false);
        showConnector = true;
        satellite.satelliteNameLabel.setVisible(false);
        primary.nameLabel.setVisible(true);
    }

    public void setPrimaryVisiblity(boolean b){
        showPrimaryForm = b;
        if(b){
            primary.showPrimary();
        }else{
            primary.hidePrimary();
        }

        if(parent != null){ //these methods get the parent's position and then slaps it with its orbit's position
            primary.deltaPosition(parent.getTranslation());
            primary.deltaPosition(satellite.getMarkerPosition());
        }
    }

    public Point2D getTranslation(){
        double x = primary.getForm().getTranslateX();
        double y = primary.getForm().getTranslateY();

        return new Point2D(x, y);
    }

    public void setSatelliteVisiblity(boolean b){
        showSatelliteForm = b;
        if(b){
            satellite.showPlanet();
        }else{
            satellite.hidePlanet();
        }
    }

    public Orbit getSatelliteFormOrbit(){
        return satellite.orbit;
    }

    public Point2D getShapeLoc(){
        return form.localToParent(shapeOffset);
    }


    public void planet_right_click(Node ellipse){

        ellipse.setOnContextMenuRequested(e -> {
            contextmenu.show(ellipse, e.getSceneX(), e.getSceneY());
            e.consume();
        });
    };

    public void setLabelParams(String name, double fontSize){
        Text t = primary.nameLabel;
        t.setFont(new Font(fontSize));
        t.setText(name);
        t.setFill(Color.WHITE);

        t = satellite.satelliteNameLabel;
        t.setFont(new Font(fontSize));
        t.setText(name);
        t.setFill(Color.WHITE);
    }

    public void setTextVisibility(Boolean b){
        primary.nameLabel.setVisible(b);
    }

    public void setBarycenter(Boolean b){
        //makes the feature into a barycenter
        if(b){
            showPrimaryForm = false;
            showSatelliteForm = true;
            satellite.hidePlanet();
            primary.setColorBlack();
        }else{
            satellite.showPlanet();
            primary.hidePrimary();
        }
        isAltForm = b;
    }

    public void setApoAndPeri(Double apogee, Double perigee){
        satellite.orbit.apogee = apogee;
        satellite.orbit.perigee = perigee;
    }

    // public void setParent(Feature parent){
    //     this.parent = parent;
    //     this.orbit.set_parent(parent);
    //     this.orbit.angle = this.angle;
    //     this.system.set_parent(parent);

    //     parent.system.add_feature(this);

    //     objectivePoint = parent.objectivePoint;

    //     //setObjectivePoint(parent.getObjectivePoint());
    // }

    public Point2D getShapeOffset(){
        return shapeOffset;
    }

    // public abstract void setShapeOffset(Point2D p);

    public Point2D getObjectivePoint(){
        return objectivePoint;
    }

    public Point2D addPoints(Point2D p, Point2D q){
        return new Point2D(p.getX() + q.getX(), p.getY() + q.getY());
    }

    // abstract public Point2D getCenterPoint(); 

    // abstract public void imbuePositioning(Boolean b);

    // abstract void liberate(); //frees a given object from its parent. Simple as

    // abstract public void setObjectivePoint(Point2D p);

    // abstract public void deltaObjPoint(Point2D p);

    // abstract void render(); //recursive! Updates the form of all features in the system

    // abstract Feature getCopy(boolean makeDeepCopy); //optionally recursive (: if lame and not recursive it just spits out a regular copy of just itself

    // abstract void deleteFeature();

    // abstract Feature cutFeature();
}