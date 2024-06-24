package com.sysedit;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class RingSys extends Feature{
    @Override
    public void render() {
        Sim sim = Sim.getSim();
        Ellipse the_ring = new Ellipse(50.0, 50.0, 50.0, 50.0);
        the_ring.setStroke(Color.BLUE);
        the_ring.setStrokeWidth(3.0);

        Group g = Sim.getSim().get_the_group();
        g.getChildren().addAll(the_ring);
    }
}
