package com.sysedit;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.MouseButton;

public abstract class Feature{
    public Feature parent;
    public Orbit orbit;
    public StarSystem system;

    public boolean is_expanded = false;
    public boolean show_orbit = true;
    public boolean show_name = true;

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

    public Point2D shapeOffset;

    public Point2D objectivePoint;

    Sim sim = Sim.getSim();

    public Feature(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rightclickcontrol.fxml"));
            contextmenu = loader.load();
            controller = loader.getController();
        } catch (IOException ex){

        }
    } 

    public void planet_right_click(Node ellipse){
        //opens menu upon right click
        ellipse.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && event.isShiftDown()) {
                try {
                    sim.open_editor(this, false);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            if (event.getButton() == MouseButton.SECONDARY) {    
                controller.set_reference(this);

                if(this.is_expanded){
                    controller.enableAddingSatellites();
                }
                if (contextmenu.isShowing()) {
                    contextmenu.hide();
                }
                contextmenu.show(ellipse, event.getScreenX(), event.getScreenY());
            }
        });
    }

    public void setParent(Feature parent){
        this.parent = parent;
        this.orbit.set_parent(parent);
        this.system.set_parent(parent);

        setObjectivePoint(parent.getObjectivePoint());
    }

    public Point2D getShapeOffset(){
        return shapeOffset;
    }

    public abstract void setShapeOffset(Point2D p);

    public Point2D getObjectivePoint(){
        return objectivePoint;
    }

    abstract public void setObjectivePoint(Point2D p);

    abstract public void deltaObjPoint(Point2D p);

    abstract void render(); //recursive! Updates the form of all features in the system
}