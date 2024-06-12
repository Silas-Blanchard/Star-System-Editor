package com.sysedit;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public abstract class Mega extends Feature{

    Mega(){
        
    }
    @Override
    public Group render() {
        Sim sim = Sim.getSim();
        Ellipse the_ring = new Ellipse(50.0, 100.0, 50.0, 100.0);
        the_ring.setStroke(Color.BLUE);
        the_ring.setStrokeWidth(3.0);

        Group g = new Group(the_ring);
        return g;
    }

    public abstract void render_megastructure();
}
