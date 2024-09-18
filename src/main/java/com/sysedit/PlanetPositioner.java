package com.sysedit;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;


public class PlanetPositioner {

    private Sim sim = Sim.getSim();

    private double deltaX;
    private double deltaY;
    private double startX;
    private double startY;

    private Circle handle;
    
    double prevX;
    double prevY;

    Point2D startObjPoint;

    PrimaryBody reference;

    Boolean isDragging = false; //keeps track of if positioner is currently being dragged. Important for limiting onMouseReleased

    public PlanetPositioner (Circle handle, PrimaryBody reference){
        this(handle, reference, false);
    }

    public PlanetPositioner (Circle ell, PrimaryBody reference, Boolean isDraggable){
        this.handle = ell;
        this.reference = reference;

        Sim sim = Sim.getSim();
        if(isDraggable){
            immbueFreeDragging();
        }
        else{
            imbueStandardDragging();
        }
    }

    public void immbueFreeDragging(){
        handle.setOnMousePressed(e ->{
            startX = e.getSceneX();
            startY = e.getSceneY();
            prevX = reference.form.getTranslateX();
            prevY = reference.form.getTranslateY();
            e.consume(); 
        }); 

        handle.setOnMouseDragged(e ->{ //setting objective point ruins everything. Access it directly.
            isDragging = true;
            deltaX = e.getSceneX() - startX;
            deltaY = e.getSceneY() - startY;

            reference.form.setTranslateX(prevX + deltaX);
            reference.form.setTranslateY(prevY + deltaY);

            //below is the logic for the connector class stored in reference.connectorIn
            Point2D movedBy = new Point2D(reference.form.getTranslateX(), reference.form.getTranslateY());

            // for(Feature f:reference.children){
            //     if(f.is_expanded){
            //         Point2D offset = f.getShapeOffset();
            //         Point2D currentPoint = new Point2D(movedBy.getX() + offset.getX(), movedBy.getY() + offset.getY());
            //         //f.connectorIn.setStart(currentPoint);
            //         //f.connectorIn.render();
            //     }
            // }

            //reference.connectorIn.setEnd(new Point2D(movedBy.getX() + reference.form.getTranslateX(), movedBy.getY() + reference.form.getTranslateY()));

            //reference.connectorIn.render();

            e.consume();
        });

        handle.setOnMouseReleased(e ->{
            if(isDragging){
                reference.objectivePoint = new Point2D(reference.form.getTranslateX(), reference.form.getTranslateY());
                e.consume();
            }
            isDragging = false;
        });
    }

    public void imbueStandardDragging(){
        handle.setOnMousePressed(e->{

        });
        handle.setOnMouseDragged(e->{
            //reference.orbit.setOrbitDegrees(angleBetweenPoints(reference.parent.objectivePoint, reference.parent.form.sceneToLocal(e.getSceneX(), e.getSceneY())));
            e.consume();
        });
    }
    
    public double angleBetweenPoints(Point2D p, Point2D q){
        double d = Math.atan2((p.getY() - q.getY()) , (p.getX() - q.getX()));

        double degrees = Math.toDegrees(d);

        degrees = (degrees + 180) % 360;
    
        return degrees;
    }
}
