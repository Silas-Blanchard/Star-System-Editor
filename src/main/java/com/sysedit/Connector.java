package com.sysedit;



import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

//this is a little class for the connecting line between an orbit and its feature (if the faeture is liberated)

public class Connector {
    public Line line;
    private Point2D start = new Point2D(0, 0);
    private Point2D end = new Point2D(0, 0);
    private Feature reference;
    private boolean show;

    public Connector(Feature f){
        Sim sim = Sim.getSim();
        line = new Line();
        line.setStroke(Color.WHITE);
        sim.add_node(line);
        show = false;
        reference = f;
    }

    public void getPoints(){
        start = reference.getObjectivePoint();
        end = reference.getShapeLoc();
    }

    public void render(){
        if(show){
            line.setStartX(start.getX());
            line.setStartY(start.getY());
            line.setEndX(end.getX());
            line.setEndY(end.getY());
        }
    }

    public void setVisible(Boolean b){
        show = b;
    }

    public void setStart(Point2D p){
        start = p;
    }

    public void setEnd(Point2D p){
        end = p;
    }
}
