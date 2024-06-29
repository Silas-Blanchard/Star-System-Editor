package com.sysedit;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;

public abstract class Feature{
    public Feature parent;
    public Orbit orbit;
    public StarSystem system;

    public boolean is_expanded = false;
    public boolean show_orbit = true;
    public boolean show_name = true;

    public double x = 0.0;
    public double y = 0.0;

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

    public Double layoutX;
    public Double layoutY;

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

        this.x = parent.x;
        this.y = parent.y;
    }

    public void set_relative_pos(double x, double y){
        layoutX = x;
        layoutY = y;
    }

    public Point2D getObjectivePoint(){
        Point2D h = form.localToScene(0,0);
        System.out.println(h);
        return h;
    }

    public void setObjectivePoint(Point2D p){
        objectivePoint = p;
        this.x = p.getX();
        this.y = p.getY();
        form.setLayoutX(x);
        form.setLayoutY(y);
    }

    public void centerOrbit(Group g){ //handles translation nice and easy
        // Point2D p = g.sceneToLocal(objectivePoint);
        // g.setTranslateX(p.getX());
        // g.setTranslateY(p.getY());
        g.setLayoutX(objectivePoint.getX());
        g.setLayoutY(objectivePoint.getY());
        // System.out.println(objectivePoint.getX() + " " + objectivePoint.getY());
        // System.out.println(x + " " + y);
    }

    abstract void render(); //recursive! Updates the form of all features in the system
}