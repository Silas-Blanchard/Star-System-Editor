package com.sysedit;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class RingSys extends Feature{
    @Override
    public void render() {
        Sim sim = Sim.getSim();
        Ellipse the_ring = new Ellipse(50.0, 50.0, 50.0, 50.0);
        the_ring.setStroke(Color.BLUE);
        the_ring.setStrokeWidth(3.0);

        Group g = Sim.getSim().get_the_group();
        g.getChildren().addAll(the_ring);
    }

        @Override
    public void setObjectivePoint(Point2D p) {
        objectivePoint = p;
        form.setLayoutX(p.getX());
        form.setLayoutY(p.getY());
    }

    @Override
    public void setShapeOffset(Point2D p) {

    }

    public void deltaObjPoint(Point2D p){
        objectivePoint = new Point2D(p.getX() + objectivePoint.getX(), p.getY() + objectivePoint.getY());
    }

    @Override
    void liberate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'liberate'");
    }

    @Override
    public void imbuePositioning() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'imbuePositioning'");
    }

    @Override
    public Point2D getCenterPoint() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCenterPoint'");
    }
}
