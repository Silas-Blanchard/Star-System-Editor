package com.sysedit;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class World extends Feature{
    public Ellipse planet = new Ellipse();
    public Positioner pos;

    World(){
        system = new StarSystem();
        orbit = new Orbit(this);
        form.getChildren().add(planet);
        system.setup_rendering(form);
    }

    @Override //will pass around a group recursively of all the non-draggable elements such as orbits
    public void render() {

        //change generation so it asks orbit where it should be and sends its angle.
        orbit.angle = this.angle;

        orbit.render();
        system.render();

        objectivePoint = new Point2D(x, y);
        
        // planet.setLayoutX(x);
        // planet.setLayoutY(y);

        planet_right_click(planet); //imbues it with being right clickable

        if(parent == null){
            pos = new Positioner(planet, this, true);
        } else{
            Point2D objective = parent.getObjectivePoint();
            form.setLayoutX(x + objective.getX());
            form.setLayoutY(y + objective.getY());
        }

        planet.setRadiusX(radius);
        planet.setRadiusY(radius);

        planet.setFill(Color.WHITE);
        planet.setStroke(Color.BLACK);
        planet.setStrokeWidth(1.0);
    }

    public void setCoord(double xPos, double yPos){
        this.x = xPos;
        this.y = yPos;

        form.setLayoutX(xPos);
        form.setLayoutY(yPos);
    }

}
