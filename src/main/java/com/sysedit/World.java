package com.sysedit;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
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

        if(is_expanded){

            orbit.render();
            system.render();

            if(parent != null){
                // Bounds b = parent.form.getLayoutBounds();
                // double newX = b.getWidth();
                // double newY = b.getHeight();
    
                // form.setTranslateX(newX);
                // form.setTranslateY(newY);

                form.getChildren().add(planet);

                Point2D objective = parent.getObjectivePoint();
                form.setLayoutX(x + objective.getX());
                form.setLayoutY(y + objective.getY());

                parent.system.remove_rendering(form);
                system.addRendering(this);

                System.out.println("form x " + form.getLayoutX() + " form y " + form.getLayoutY());
            }

            objectivePoint = new Point2D(x, y);

            pos = new Positioner(planet, this, true);

        } else {
            //change generation so it asks orbit where it should be and sends its angle.
            orbit.angle = this.angle;

            orbit.render();

            objectivePoint = new Point2D(x, y);
            
            // planet.setLayoutX(x);
            // planet.setLayoutY(y);

            Point2D objective = parent.getObjectivePoint();
            form.setLayoutX(x + objective.getX());
            form.setLayoutY(y + objective.getY());
        }

        planet_right_click(planet); //imbues it with being right clickable

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
