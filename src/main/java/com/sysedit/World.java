package com.sysedit;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class World extends Feature{

    World(){
        orbit = new Orbit(this);
        system = new StarSystem();
    }

    @Override
    public Group render() {
        //change generation so it asks orbit where it should be and sends its angle. 
        Ellipse ellipse = new Ellipse(radius, radius, radius, radius);
        ellipse.setFill(Color.WHITE);

        planet_right_click(ellipse);
        x = ellipse.getCenterX();
        y = ellipse.getCenterY();

        Group g = new Group(ellipse, orbit.render(), system.render());

        for (Feature f: system.get_features() ){
            g.getChildren().addAll(f.render());
        }

        return g;
    }
    
}
