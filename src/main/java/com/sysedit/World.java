package com.sysedit;

import java.util.ArrayList;

import javafx.geometry.Bounds;
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
        planet_right_click(planet); //imbues it with being right clickable
        objectivePoint = new Point2D(0, 0);
        setObjectivePoint(objectivePoint);
    }

    @Override //will pass around a group recursively of all the non-draggable elements such as orbits
    public void render() {
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

            //setObjectivePoint(parent.getObjectivePoint());

            connectorIn.setStart(objectivePoint);

        }

        planet.setRadiusX(radius);
        planet.setRadiusY(radius);

        planet.setFill(Color.WHITE);
        planet.setStroke(Color.BLACK);
        planet.setStrokeWidth(1.0);

        // if(parent != null && parent.getObjectivePoint() != null){
        //     Point2D h = parent.getObjectivePoint();
        //     form.setLayoutX(h.getX());
        //     form.setLayoutY(h.getY());
        // }
    }

    @Override
    public void imbuePositioning(){
        PlanetPositioner pos = new PlanetPositioner(planet, this, true);
    }

    @Override
    public void setObjectivePoint(Point2D p) {
        this.objectivePoint = new Point2D(p.getX(), p.getY());
        form.setTranslateX(p.getX());
        form.setTranslateY(p.getY());
    }

    @Override
    public void setShapeOffset(Point2D p) {
        planet.setTranslateX(p.getX());
        planet.setTranslateY(p.getY());
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
            objectivePoint = parent.getObjectivePoint();
            //setObjectivePoint(objective);

            system.addRendering(this);
        }

        planet.setTranslateX(0);
        planet.setTranslateY(0);

        imbuePositioning();

        connectorIn.setVisible(true);
        connectorIn.setStart(addPoints(shapeOffset, objectivePoint));
        connectorIn.setEnd(objectivePoint);

        system.subgroup.setTranslateX(parent.system.subgroup.getTranslateX() + shapeOffset.getX());
        system.subgroup.setTranslateY(parent.system.subgroup.getTranslateY() + shapeOffset.getY());
    }

    @Override
    public Point2D getCenterPoint(){
        Bounds boundsInScene = form.localToScene(form.getBoundsInLocal());
        double centerX = (boundsInScene.getMinX() + boundsInScene.getMaxX()) / 2;
        double centerY = (boundsInScene.getMinY() + boundsInScene.getMaxY()) / 2;

        return new Point2D(centerX, centerY);
    }
}
