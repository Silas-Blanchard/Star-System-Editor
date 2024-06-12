package com.sysedit;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;

public abstract class Feature{
    public Feature parent;
    public Orbit orbit;
    public StarSystem system;

    public boolean is_expanded = true;
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
                System.out.println(controller);
                if (contextmenu.isShowing()) {
                    contextmenu.hide();
                }
                contextmenu.show(ellipse, event.getScreenX(), event.getScreenY());
            }
        });
    }

    public void set_parent(Feature parent){
        this.parent = parent;
        this.orbit.parent = parent;
    }


    abstract Group render(); //recursive!
}