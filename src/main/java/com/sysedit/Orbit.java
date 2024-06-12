package com.sysedit;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class Orbit {
    Double angle;
    Double inclination;
    Double apogee;
    Double perigee;
    Feature parent;

    Orbit(Feature parent){
        //defaults
        this.angle = 0.0;
        this.inclination = 0.0;
        this.apogee = 100.0;
        this.perigee = 100.0;
        this.parent = parent;
    }

    public Group render(){
        if (parent.show_orbit){
            Ellipse i = new Ellipse(100.0, 100.0, 100.0, 100.0);
            i.setStrokeWidth(1.0);
            i.setStroke(Color.WHITE);
            i.setFill(Color.TRANSPARENT);
            i.setCenterX(parent.x);
            i.setCenterY(parent.y);
            i.setMouseTransparent(true);
            return new Group(i);
        }
        return new Group();
    }
}
