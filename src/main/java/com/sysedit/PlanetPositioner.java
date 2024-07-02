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

    public PlanetPositioner (Ellipse handle, World reference){
        this(handle, reference, false);
    }

    public PlanetPositioner (Ellipse ell, World reference, Boolean isDraggable){
        this.handle = ell;


        if(isDraggable){
            handle.setOnMousePressed(e ->{
                startX = e.getSceneX();
                startY = e.getSceneY();
                prevX = reference.system.subgroup.getLayoutX();
                prevY = reference.system.subgroup.getLayoutY();
                e.consume(); 
            }); 

            handle.setOnMouseDragged(e ->{
                deltaX = e.getSceneX() - startX;
                deltaY = e.getSceneY() - startY;

                reference.system.subgroup.setLayoutX(prevX + deltaX);
                reference.system.subgroup.setLayoutY(prevY + deltaY);

                reference.connecta.render();

                e.consume();
            });

            handle.setOnMouseDragReleased(e ->{
                reference.deltaObjPoint(new Point2D(prevX + deltaY, prevY + deltaX));
                e.consume();
            });
        }
        else{
            handle.setOnMousePressed(null);
            handle.setOnMouseDragged(null);
        }
    }
}
