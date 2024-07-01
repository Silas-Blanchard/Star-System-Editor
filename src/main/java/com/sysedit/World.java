package com.sysedit;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class World extends Feature{
    public Ellipse planet = new Ellipse();
    public Positioner pos;

    World(){
        system = new StarSystem();
        orbit = new Orbit(this);
        system.setup_rendering(form);
    }

    @Override //will pass around a group recursively of all the non-draggable elements such as orbits
    public void render() {
        //setObjectivePoint(getObjectivePoint());
        if(form.getChildren().size() == 0){
            form.getChildren().add(planet);
        }

        if(is_expanded){

            orbit.render();
            system.render();

        } else {
            //change generation so it asks orbit where it should be and sends its angle.
            orbit.angle = this.angle;

            orbit.render();

            Point2D objective = parent.getObjectivePoint();
            setObjectivePoint(objective);
        }

        planet_right_click(planet); //imbues it with being right clickable

        planet.setRadiusX(radius);
        planet.setRadiusY(radius);

        planet.setFill(Color.WHITE);
        planet.setStroke(Color.BLACK);
        planet.setStrokeWidth(1.0);
    }

    @Override
    public void setObjectivePoint(Point2D p) {
        objectivePoint = p;
        form.setLayoutX(p.getX());
        form.setLayoutY(p.getY());
    }

    @Override
    public void setShapeOffset(Point2D p) {
        planet.setLayoutX(p.getX());
        planet.setLayoutY(p.getY());
        this.shapeOffset = p;
    }

    @Override
    public void deltaObjPoint(Point2D p){
        objectivePoint = new Point2D(p.getX() + objectivePoint.getX(), p.getY() + objectivePoint.getY());
    }

    @Override
    public void liberate(){
        //this will make the world its own system. That simple.
        //this.orbit STAYS with the parent's group
        if(this.system.equals(parent.system)){
            this.system = new StarSystem();
            parent.system.remove_rendering(this);
        }

        if(parent != null){
            Point2D objective = parent.getObjectivePoint();
            setObjectivePoint(objective);

            system.addRendering(this);
        }

        pos = new Positioner(planet, this, true);
    }
}
