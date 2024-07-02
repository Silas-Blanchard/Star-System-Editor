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

    public PlanetPositioner (Ellipse handle, World reference){
        this(handle, reference, false);
    }

    public PlanetPositioner (Ellipse ell, World reference, Boolean isDraggable){
        this.handle = ell;

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
                deltaX = e.getSceneX() - startX;
                deltaY = e.getSceneY() - startY;

                reference.system.subgroup.setLayoutX(prevX + deltaX);
                reference.system.subgroup.setLayoutY(prevY + deltaY);

                //reference.connectorIn.renderInDrag(prevX + deltaX + reference.objectivePoint.getX(), prevY + deltaY + reference.objectivePoint.getY());

                for(Feature f:reference.system.features){
                    System.out.println("Wumbo");
                    if(f.is_expanded){
                        f.connectorIn.setStart(f.getShapeLoc());
                        reference.connectorIn.render();
                    }
                }  

                Point2D gah = new Point2D(prevX + deltaX + reference.objectivePoint.getX(), prevY + deltaY + reference.objectivePoint.getY());
                reference.connectorIn.setEnd(gah);

                reference.connectorIn.render();

                System.out.println(reference.connectorIn.line);

                e.consume();
            });

            handle.setOnMouseDragReleased(e ->{
                reference.deltaObjPoint(new Point2D(prevX + deltaY, prevY + deltaX));
                //reference.connectorIn.render();
                e.consume();
            });
        }
        else{
            handle.setOnMousePressed(null);
            handle.setOnMouseDragged(null);
        }
    }
}
