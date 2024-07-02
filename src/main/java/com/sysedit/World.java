package com.sysedit;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class World extends Feature{
    public Ellipse planet = new Ellipse();
    public PlanetPositioner pos;

    World(){
        system = new StarSystem();
        orbit = new Orbit(this);
        system.setup_rendering(form);
        this.connectorIn = new Connector(this);
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

    public void returnToZero(){
        //this just makes the current planet offset + objective point be the new objective point
        setObjectivePoint(new Point2D(shapeOffset.getX() + objectivePoint.getX(), shapeOffset.getY() + objectivePoint.getY()));
        setShapeOffset(new Point2D(0, 0));
    }

    @Override
    public void imbuePositioning(){
        PlanetPositioner pos = new PlanetPositioner(planet, this, true);
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

        connectorIn.setVisible(true);
        imbuePositioning();
        returnToZero();
    }
}
