package com.sysedit;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Ellipse;


public class Positioner {

    private double deltaX;
    private double deltaY;
    private Group the_group;
    private Sim sim;
    private Feature reference;
    private double startX;
    private double startY;

    private Ellipse handle;
    
    double prevX;
    double prevY;

    public Positioner (Ellipse handle, Feature reference){
        this(handle, reference, false);
    }

    public Positioner (Ellipse ell, Feature reference, Boolean isDraggable){
        sim = Sim.getSim();
        this.reference = reference;
        this.the_group = sim.get_the_group();

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

                // handle.setTranslateX(deltaX);
                // handle.setTranslateY(deltaY);
                
                // for (Node n : reference.system.objects){
                //     n.setTranslateX(deltaX);
                //     n.setTranslateY(deltaY);
                // }



                reference.system.subgroup.setLayoutX(prevX + deltaX);
                reference.system.subgroup.setLayoutY(prevY + deltaY);

                reference.x = prevX + deltaX;
                reference.y = prevY + deltaY;

                reference.objectivePoint = reference.form.localToParent(new Point2D(reference.x, reference.y));

                // Point2D handleScenePosition = handle.localToParent(0, 0); //this is the handles local position+
                // reference.setObjectivePoint(handleScenePosition);

                // System.out.println("start X " + startX +" start Y: " + startY);
                // System.out.println("e X " + e.getSceneX() +" e Y: " + e.getSceneY());
                // System.out.println("Delta X: " + deltaX+" Delta Y: " + deltaY);
                // System.out.println("Ref X: " + reference.x+" Ref Y: " + reference.y);
                // System.out.println("Prev X: " + prevX +" Prev Y: " + prevY);

                e.consume();
            });
        }
        else{
            handle.setOnMousePressed(null);
            handle.setOnMouseDragged(null);
        }
    }
}
