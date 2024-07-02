package com.sysedit;



import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Line;

//this is a little class for the connecting line between an orbit and its feature (if the faeture is liberated)

public class Connector {
    private Line line;
    private Point2D start;
    private Point2D end;
    private Feature reference;
    private boolean show;

    public Connector(Feature f){
        Sim sim = Sim.getSim();
        line = new Line();
        sim.add_node(line);
        show = false;
        reference = f;
    }

    public void getPoints(){
        start = reference.objectivePoint;
        end = new Point2D(reference.objectivePoint.getX() + reference.shapeOffset.getX() , reference.objectivePoint.getY() + reference.shapeOffset.getY());
    }

    public void render(){
        if(show){
            getPoints();
            line.setStartX(start.getX());
            line.setStartY(start.getY());
            line.setEndX(end.getX());
            line.setEndY(end.getY());
        }
    }

    public void setVisible(Boolean b){
        show = b;
    }
}
