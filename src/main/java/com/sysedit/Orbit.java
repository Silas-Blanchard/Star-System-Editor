package com.sysedit;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.lang.Math; 

public class Orbit {
    public Double angle;
    public Double inclination;
    public Double apogee;
    public Double perigee;
    public Feature parent;
    public Feature reference;

    private Double planet_x;
    private Double planet_y;

    Orbit(Feature reference){
        //defaults
        this.angle = 0.0;
        this.inclination = 0.0;
        this.apogee = reference.apogee;
        this.perigee = reference.perigee;

        this.reference = reference;
        this.parent = reference.parent;
    }

    public Group render(){
        if (parent != null && reference.show_orbit){
            Ellipse i = getOrbit();
            i.setStrokeWidth(2.0);
            i.setStroke(Color.WHITE);
            i.setFill(Color.TRANSPARENT);
            i.setCenterX(parent.x);
            i.setCenterY(parent.y);
            i.setMouseTransparent(true);

            return new Group(i, renderPlanet(i));
        }
        return new Group();
    }

    private Group renderPlanet(Ellipse orbit){
        //I want to explain this bit
        //will create a line that intersects the orbit and then finds where it intersects. Never returns the line.
        Double a = reference.apogee;
        Double p = reference.perigee;

        Double angle = Math.toRadians(this.angle);

        Double eccentricity = (a - p) / (a + p);
        Double semimajor = (a + p) / 2;
        Double semiminor = semimajor * Math.sqrt(1 - eccentricity * eccentricity);

        //by their powers combined, they can calculate the equation of the ellipse!
        Double r = semiminor / Math.sqrt(1 - (eccentricity * Math.cos(angle)) * (eccentricity * Math.cos(angle)));
        //r is the distance to the point! Now! To convert it to a solid number of pixels!

        planet_x = r * Math.cos(angle) + parent.x;
        planet_y = r * Math.sin(angle) + parent.y;

        reference.x = planet_x;
        reference.y = planet_y;

        Circle point = new Circle();
        point.setCenterX(planet_x);
        point.setCenterY(planet_y);
        point.setRadius(1);
        point.setFill(Color.WHITE);

        Circle outline = new Circle();
        outline.setCenterX(planet_x);
        outline.setCenterY(planet_y);
        outline.setRadius(5);
        outline.setFill(Color.TRANSPARENT);
        outline.setStrokeWidth(1.0);
        outline.setStroke(Color.WHITE);
        //THIS NEEDS TO TAKE INTO ACCOUNT THE ROTATION

        return new Group(point, outline);
    }

    private Ellipse getOrbit(){
        Double a = reference.apogee;
        Double p = reference.perigee;

        Double eccentricity = (a - p) / (a + p);
        Double semimajor = (a + p) / 2;
        Double semiminor = semimajor * Math.sqrt(1 - eccentricity * eccentricity);

        Ellipse orbit = new Ellipse(reference.x, reference.y, semimajor, semiminor);

        Rotate rotateTransform = new Rotate(75, 0, 0, 0, Rotate.X_AXIS);
        orbit.getTransforms().add(rotateTransform);

        return orbit;
    }
}
