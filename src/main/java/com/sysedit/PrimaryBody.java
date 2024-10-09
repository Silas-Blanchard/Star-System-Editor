package com.sysedit;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class PrimaryBody {
    public ArrayList<SatelliteBody> satellites;
    public Group form;
    public Group crosshair;
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
        form.getChildren().addAll(nameLabel, shape);
        DragImbuer d = new DragImbuer(nameLabel);
        shape.setFill(Color.WHITE);

        //little plus sign signifying the center
        Line line1 = new Line(-5,0,5,0);
        Line line2 = new Line(0,-5,0,5);
        line1.setStroke(Color.WHITE);
        line2.setStroke(Color.WHITE);
        crosshair = new Group();
        crosshair.getChildren().addAll(line1, line2);
        form.getChildren().addAll(crosshair);
        line1.setViewOrder(0.1);
        line2.setViewOrder(0.1);

        hidePrimary();
    }

    public Group getForm(){
        return form;
    }

    public void showPrimary(){
        if(!form.getChildren().contains(shape)){
            form.getChildren().add(shape);
            crosshair.setVisible(true);
        }
    }

    public void hidePrimary(){ //hiding the primary actually removes it so no one can click it (maybe invisible things can be clicked idk)
        if(form.getChildren().contains(shape)){
            form.getChildren().remove(shape);
            crosshair.setVisible(false);
        }
    }

    public void setColorBlack(){
        shape.setFill(Color.BLACK);
        shape.setViewOrder(1.0);
    }

    public void render(){
        if(reference.isAltForm){
            nameLabel.setVisible(false);
        }
    }

    public void deltaPosition(Point2D p){
        form.setTranslateX(form.getTranslateX() + p.getX());
        form.setTranslateY(form.getTranslateY() + p.getY());
    }
}
