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
                prevX = reference.system.subgroup.getLayoutX();
                prevY = reference.system.subgroup.getLayoutY();
                e.consume(); 
            }); 

            handle.setOnMouseDragged(e ->{ //setting objective point ruins everything. Access it directly.
                isDragging = true;
                deltaX = e.getSceneX() - startX;
                deltaY = e.getSceneY() - startY;

                reference.system.subgroup.setLayoutX(prevX + deltaX);
                reference.system.subgroup.setLayoutY(prevY + deltaY);

                //reference.connectorIn.renderInDrag(prevX + deltaX + reference.objectivePoint.getX(), prevY + deltaY + reference.objectivePoint.getY());

                // if(reference.parent != null){
                //     for(Feature f:reference.parent.system.features){
                //         if(f.is_expanded){
                //             f.connectorIn.setStart(f.getShapeLoc());
                //             reference.connectorIn.render();
                //         }
                //     }
                // }

                for(Feature f:reference.system.features){
                    if(f.is_expanded){
                        Point2D offset = f.getShapeOffset();
                        //Point2D offset = f.getShapeLoc();
                        Point2D obj = f.getObjectivePoint();
                        Point2D currentPoint = new Point2D(prevX + deltaX + obj.getX(), prevY + deltaY + obj.getY());
                        //Point2D currentPoint = new Point2D(prevX + deltaX + offset.getX(), prevY + deltaY + offset.getY());
                        f.connectorIn.setStart(currentPoint);
                        f.connectorIn.render();
                    }
                }

                if(reference.parent != null){
                    Point2D obj = reference.getObjectivePoint();
                    //reference.connectorIn.setStart(new Point2D(prevX + deltaX + obj.getX(), prevY + deltaY + obj.getY()));
                    reference.connectorIn.setStart(obj);
                }

                Point2D gah = new Point2D(prevX + deltaX + reference.objectivePoint.getX(), prevY + deltaY + reference.objectivePoint.getY());
                reference.connectorIn.setEnd(gah);

                reference.connectorIn.render();

                //System.out.println(reference.connectorIn.line);

                e.consume();
            });

            handle.setOnMouseReleased(e ->{
                if(isDragging){
                    reference.setObjectivePoint(reference.form.localToParent(new Point2D(prevX + deltaX, prevY + deltaY)));
                    e.consume();
                }
                
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
