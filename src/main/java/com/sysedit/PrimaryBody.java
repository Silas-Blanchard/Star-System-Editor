package com.sysedit;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class PrimaryBody {
    public ArrayList<SatelliteBody> satellites;
    public Group form;
    public Circle shape = new Circle(10);
    public Feature reference;

    public Point2D objectivePoint;

    public Text nameLabel;

    public PrimaryBody(Feature f){
        satellites = new ArrayList<SatelliteBody>();
        this.reference = f;
        PlanetPositioner pos = new PlanetPositioner(shape, this, true);

        nameLabel = new Text();

        form = new Group();
        form.getChildren().addAll(shape, nameLabel);
        DragImbuer d = new DragImbuer(nameLabel);
        shape.setFill(Color.WHITE);

        hidePrimary();
    }

    public Group getForm(){
        return form;
    }

    public void showPrimary(){
        if(!form.getChildren().contains(shape)){
            form.getChildren().add(shape);
        }
    }

    public void hidePrimary(){
        if(form.getChildren().contains(shape)){
            form.getChildren().remove(shape);
        }
    }

    public void setColorBlack(){
        shape.setFill(Color.BLACK);
        shape.setViewOrder(1.0);
    }

    public void render(){

    }

    public void deltaPosition(Point2D p){
        form.setTranslateX(form.getTranslateX() + p.getX());
        form.setTranslateY(form.getTranslateY() + p.getY());
    }
}
