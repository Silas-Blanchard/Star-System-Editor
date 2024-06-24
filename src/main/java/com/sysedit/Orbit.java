package com.sysedit;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
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
    public World reference;

    private Double planet_x;
    private Double planet_y;

    private Feature parent;
    public Rotate rotateTransform;

    public Group form = new Group();

    private Group planetMarker;
    private Ellipse orbit_ellipse;

    Orbit(World reference){
        //defaults
        this.angle = 0.0;
        this.inclination = 0.0;
        this.apogee = reference.apogee;
        this.perigee = reference.perigee;

        this.reference = reference;
        this.parent = reference.parent;

        reference.system.setup_rendering(form);
    }

    public void render(){
        //For some reason it does not agree with moving the planet. Seems to be pushed way to the left and the marker is similarly lost
        if (reference.show_orbit){
            Ellipse i = getOrbit();
            i.setStrokeWidth(2.0);
            i.setStroke(Color.WHITE);
            i.setFill(Color.TRANSPARENT);
            i.setMouseTransparent(true);

            form.getChildren().clear();

            planetMarker = renderPlanet(i, rotateTransform);
            form.getChildren().add(planetMarker);

            orbit_ellipse = i;
            form.getChildren().add(orbit_ellipse);
        }
        if(parent != null && parent.getObjectivePoint() != null){
            parent.transLocal(form);
        }
    }

    private Group renderPlanet(Ellipse orbit, Rotate rotate){
        //bounds to find true length and height
        Bounds bounds = orbit.localToScene(orbit.getBoundsInLocal());
        double updatedWidth = bounds.getWidth();
        double updatedHeight = bounds.getHeight();

        //I want to explain this bit
        Double radiusX = updatedWidth / 2;
        Double radiusY = updatedHeight / 2;

        Double semimajor;
        Double semiminor;

        if(radiusX > radiusY){
            semimajor = radiusX;
            semiminor = radiusY;
        }else{
            semimajor = radiusY;
            semiminor = radiusX;
        }

        Double angle = Math.toRadians(this.angle);

        Double eccentricity = Math.sqrt(1 - (semiminor * semiminor) / (semimajor * semimajor));

        //by their powers combined, they can calculate the equation of the ellipse!
        Double r = semiminor / Math.sqrt(1 - (eccentricity * Math.cos(angle)) * (eccentricity * Math.cos(angle)));
        //r is the distance to the point! Now! To convert it to a solid number of pixels!
        
        //this equation is relative to the center of the orbit! I used to assume the parent was always the center!
        planet_x = r * Math.cos(angle) + form.getLayoutX();
        planet_y = r * Math.sin(angle) + form.getLayoutY();

        reference.setCoord(planet_x, planet_y); //reference is moved relative to parent

        Circle point = new Circle();
        point.setLayoutX(planet_x - form.getLayoutX()); //point is moved relative to the center of the group
        point.setLayoutY(planet_y);

        point.setRadius(1);
        point.setFill(Color.WHITE);

        Circle outline = new Circle();
        outline.setLayoutX(planet_x - form.getLayoutX());
        outline.setLayoutY(planet_y);
        outline.setRadius(5);
        outline.setFill(Color.TRANSPARENT);
        outline.setStrokeWidth(1.0);
        outline.setStroke(Color.WHITE);

        return new Group(point, outline);
    }

    private Ellipse getOrbit(){
        Double a = reference.apogee;
        Double p = reference.perigee;

        Double eccentricity = (a - p) / (a + p);
        Double semimajor = (a + p) / 2;
        Double semiminor = semimajor * Math.sqrt(1 - eccentricity * eccentricity);

        Ellipse orbit = new Ellipse(0, 0, semimajor, semiminor);

        orbit.getTransforms().add(rotateTransform);

        //this bit is for putting the parent in the focus of the ellipse
        Double focalLength = Math.sqrt(semimajor * semimajor - semiminor * semiminor);
        form.setLayoutX(focalLength);

        return orbit;
    }
    
    public void set_parent(Feature parent){
        this.parent = parent;
        rotateTransform = new Rotate(75, 0, 0, 0, Rotate.X_AXIS);
    }
}
