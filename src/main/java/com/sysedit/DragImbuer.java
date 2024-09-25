package com.sysedit;

import javafx.scene.Node;

public class DragImbuer {
    private double initX;
    private double initY;
    public DragImbuer(Node node){


        node.setOnMousePressed(e ->{
            initX = e.getSceneX() - node.getTranslateX();
            initY = e.getSceneY() - node.getTranslateY();
            e.consume();
        });

        node.setOnMouseDragged(e ->{
            node.setTranslateX(e.getSceneX() - initX);
            node.setTranslateY(e.getSceneY() - initY);
            e.consume();
        });
    }
}
