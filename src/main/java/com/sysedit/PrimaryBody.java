package com.sysedit;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PrimaryBody {
    public ArrayList<SatelliteBody> satellites;
    public Group form;
    public Circle shape = new Circle(10);
    public Feature reference;

    public Point2D objectivePoint;

    public PrimaryBody(Feature f){
        satellites = new ArrayList<SatelliteBody>();
        this.reference = f;
        PlanetPositioner pos = new PlanetPositioner(shape, this, true);

        form = new Group();
        form.getChildren().add(shape);
    }

    public Group getForm(){
        return form;
    }

    public void render(){
        shape.setFill(Color.WHITE);
    }
}
