package com.sysedit;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
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
    public void imbuePositioning(Boolean b){
        PlanetPositioner pos = new PlanetPositioner(planet, this, b);
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

        imbuePositioning(true);

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

    @Override
    public Feature getCopy(boolean makeDeepCopy) {
        if(makeDeepCopy){
            return getDeepCopy();
        }
        return internalGetCopy();
    }

    private World internalGetCopy(){
        World newWorld = new World();
        newWorld.angle = this.angle;
        newWorld.apogee = this.apogee;
        newWorld.perigee = this.perigee;
        newWorld.name = this.name;
        newWorld.radius = this.radius;

        newWorld.show_orbit = true;
        newWorld.is_expanded = false;

        newWorld.setParent(this.parent);
        newWorld.imbuePositioning(false);

        return newWorld;
    }

    private Feature getDeepCopy(){
        World newWorld = internalGetCopy();

        for(Feature f : this.system.features){
            Feature fCopy = f.getCopy(true);
            fCopy.setParent(newWorld);
            newWorld.system.add_feature(fCopy);
        }

        return newWorld;
    }

    @Override
    void deleteFeature() {
        this.system.deleteFeatureRendering();
        if(parent != null){
            parent.system.removeFeature(this);
            parent.system.remove_rendering(this);
            parent.render();
        }
        if(this == sim.getSystemParent()){
            sim.set_new_parent();
        }

        for(Node i:sim.get_the_group().getChildren()){
            if(javafx.scene.Group.class.isInstance(i)){
                for(Node j:sim.get_the_group().getChildren()){
                    System.out.println(j);
                }
            }
        }
    }

    @Override
    Feature cutFeature() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cutFeature'");
    }

}
