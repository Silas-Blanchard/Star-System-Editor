package com.sysedit;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PrimaryBody {
    public ArrayList<SatelliteBody> satellites;
    public Group form;

    public PrimaryBody(){
        satellites = new ArrayList<SatelliteBody>();
    }

    public Group getForm(){
        Group g = new Group();
        Circle c = new Circle(10);
        c.setFill(Color.WHITE);
        g.getChildren().add(c);
        return g;
    }
}
