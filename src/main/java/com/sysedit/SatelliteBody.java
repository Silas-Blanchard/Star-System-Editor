package com.sysedit;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SatelliteBody {
    Group form;
    Orbit orbit;
    Circle body;

    public SatelliteBody(){
        orbit = new Orbit();
        body = new Circle(10);
        body.setFill(Color.WHITE);
    }

    public Group getForm(){
        Group g = new Group();
        g.getChildren().add(orbit.getForm(body));
        return g;
    }

    public void setBodyAsPlanet(Double radius){
        body = new Circle(radius);
    }
}
