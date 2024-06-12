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
        orbit.angle = this.angle;
        Group g = new Group(orbit.render(), system.render());

        Ellipse ellipse = new Ellipse(radius, radius, radius, radius);
        ellipse.setFill(Color.WHITE);
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(1.0);

        planet_right_click(ellipse);
        ellipse.setCenterX(x);
        ellipse.setCenterY(y);

        g.getChildren().add(ellipse);

        for (Feature f: system.get_features() ){
            g.getChildren().addAll(f.render());
        }
        System.out.println(system.get_features());
        System.out.println(this);
        return g;
    }
    
}
