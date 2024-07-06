package com.sysedit;

import javafx.geometry.Point2D;
import javafx.scene.shape.Ellipse;


public class PlanetPositioner {

    private double deltaX;
    private double deltaY;
    private double startX;
    private double startY;

    private Ellipse handle;
    
    double prevX;
    double prevY;

    Point2D startObjPoint;

    Feature reference;

    Boolean isDragging = false; //keeps track of if positioner is currently being dragged. Important for limiting onMouseReleased

    public PlanetPositioner (Ellipse handle, World reference){
        this(handle, reference, false);
    }

    public PlanetPositioner (Ellipse ell, World reference, Boolean isDraggable){
        this.handle = ell;
        this.reference = reference;

        Sim sim = Sim.getSim();
        if(isDraggable){
            handle.setOnMousePressed(e ->{
                startX = e.getSceneX();
                startY = e.getSceneY();
                prevX = reference.system.subgroup.getTranslateX();
                prevY = reference.system.subgroup.getTranslateY();
                e.consume(); 
            }); 

            handle.setOnMouseDragged(e ->{ //setting objective point ruins everything. Access it directly.
                isDragging = true;
                deltaX = e.getSceneX() - startX;
                deltaY = e.getSceneY() - startY;

                reference.system.subgroup.setTranslateX(prevX + deltaX);
                reference.system.subgroup.setTranslateY(prevY + deltaY);

                //below is the logic for the connector class stored in reference.connectorIn
                Point2D movedBy = new Point2D(reference.system.subgroup.getTranslateX(), reference.system.subgroup.getTranslateY());

                for(Feature f:reference.system.features){
                    if(f.is_expanded){
                        Point2D offset = f.getShapeOffset();
                        Point2D currentPoint = new Point2D(movedBy.getX() + offset.getX(), movedBy.getY() + offset.getY());
                        f.connectorIn.setStart(currentPoint);
                        f.connectorIn.render();
                    }
                }

                reference.connectorIn.setEnd(new Point2D(movedBy.getX() + reference.form.getTranslateX(), movedBy.getY() + reference.form.getTranslateY()));

                reference.connectorIn.render();

                e.consume();
            });

            handle.setOnMouseReleased(e ->{
                if(isDragging){
                    //reference.setObjectivePoint(reference.form.localToParent(new Point2D(prevX + deltaX, prevY + deltaY)));
                    reference.objectivePoint = new Point2D(reference.system.subgroup.getTranslateX(), reference.system.subgroup.getTranslateY());
                    // reference.objectivePoint = reference.getCenterPoint();
                    // System.out.println(reference.objectivePoint);
                    // System.out.println(reference.getCenterPoint());
                    e.consume();
                    // System.out.println("Hey ya");
                }

                //USE relocate() as it accounts for minX which is not currently accounted for. 
                
                isDragging = false;
                //reference.connectorIn.render();

                //this needs to reset the values mayeb that will help
                //System.out.println(reference.getObjectivePoint());
            });
        }
        else{
            handle.setOnMousePressed(null);
            handle.setOnMouseDragged(null);
        }
    }
}
