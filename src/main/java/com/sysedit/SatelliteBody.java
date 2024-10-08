package com.sysedit;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

public class SatelliteBody {
    Group form;
    Orbit orbit;
    Circle shape;
    Feature reference;
    Group orbitGroup;
    Text satelliteNameLabel;

    public SatelliteBody(Feature f){
        reference = f;
        orbit = new Orbit();
        shape = new Circle(10);

        form = new Group();
        orbitGroup = new Group();
        
        satelliteNameLabel = new Text();
        satelliteNameLabel.setVisible(true);
        DragImbuer d = new DragImbuer(satelliteNameLabel);

        form.getChildren().addAll(orbitGroup, satelliteNameLabel);

        shape.setFill(Color.WHITE);
    }

    public void render(){
        form.getChildren().remove(orbitGroup);
        if(reference.isAltForm){
            orbitGroup = orbit.getForm(reference.altform);
        }else{
            orbitGroup = orbit.getForm(shape);
        }

        form.getChildren().add(orbitGroup);
    }

    public Group getForm(){
        return form;
    }

    public void setBodyAsPlanet(Double radius){
        shape = new Circle(radius);
    }

    public void hidePlanet(){
        if(orbitGroup.getChildren().contains(shape)){
            orbitGroup.getChildren().remove(shape);
        }
    }

    public void showPlanet(){
        if(!orbitGroup.getChildren().contains(shape)){
            orbitGroup.getChildren().add(shape);
        }
    }

    public Point2D getMarkerPosition(){
        //this is relative to the primary form of the parent
        return orbit.planetPoint;
    }
}
