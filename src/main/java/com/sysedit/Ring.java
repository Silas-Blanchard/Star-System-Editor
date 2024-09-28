package com.sysedit;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class Ring {
    Feature parent;
    boolean showOrbit;
    boolean showName;
    Color color;
    Group form;
    Shape belt;

    double width;
    double opacity;
    double viewAngle;
    double inclination;

    Text name;

    public Ring(){
        Ellipse outer = new Ellipse(0, 0, 250, 250);
        Ellipse inner = new Ellipse(0, 0, 150, 150);

        opacity = 10;
        color = Color.rgb(20,255,50, opacity / 100);
        
        Shape belt = Shape.subtract(outer, inner);
        belt.setFill(color);

        Rotate rotateTransform = new Rotate(75, 0, 0, 0, Rotate.X_AXIS);
        belt.getTransforms().addAll(rotateTransform);

        name = new Text("Belt");
        name.setVisible(false);

        form = new Group();

        form.getChildren().addAll(belt, name);
    }

    public void assignParent(Feature f){
        this.parent = f;
    }
}
