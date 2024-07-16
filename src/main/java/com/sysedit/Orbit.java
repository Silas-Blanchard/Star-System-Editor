package com.sysedit;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.lang.Math;

public class Orbit {
    public Double angle;
    public Double inclination;
    public Double yaw;
    public Double apogee;
    public Double perigee;
    public World reference;

    private Double planet_x;
    private Double planet_y;

    private Feature parent;
    public Rotate rotateTransform;
    public Rotate inclinationTransform;
    public Rotate yawTransform;

    public Group form = new Group();

    private Group planetMarker;
    private Ellipse orbit_ellipse;

    public OrbitManager orbitManagr;
    public Rectangle orbitManagingButton = new Rectangle(10, 10);

    Orbit(World reference){
        //defaults
        this.angle = 0.0;
        this.inclination = 0.0;
        this.yaw = 0.0;
        this.apogee = reference.apogee;
        this.perigee = reference.perigee;

        this.reference = reference;
        this.parent = reference.parent;

        reference.system.setup_rendering(form);

        rotateTransform = new Rotate(75, 0, 0, 0, Rotate.X_AXIS);
        inclinationTransform = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        yawTransform = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

        orbitManagr = new OrbitManager(reference, this);
    }

    public void render(){
        //For some reason it does not agree with moving the planet. Seems to be pushed way to the left and the marker is similarly lost
        if (reference.show_orbit){
            form.getChildren().clear();

            getOrbit();
            orbit_ellipse.setStrokeWidth(2.0);
            orbit_ellipse.setStroke(Color.WHITE);
            orbit_ellipse.setFill(Color.TRANSPARENT);
            orbit_ellipse.setMouseTransparent(true);

            renderPlanet(orbit_ellipse);

            orbitManagr.setButtonVisible(reference.show_orbit);
        }
    }

    private void renderPlanet(Ellipse orbit){
        //All of this takes place in the local coord system of the orbit!
        Double radiusX = orbit.getRadiusX();
        Double radiusY = orbit.getRadiusY();

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
        
        planet_x = r * Math.cos(angle);
        planet_y = r * Math.sin(angle);

        Point2D adjustedPoint = orbit.localToParent(planet_x, planet_y);

        reference.setShapeOffset(adjustedPoint);

        Circle point = new Circle();
        point.setRadius(1);
        point.setFill(Color.WHITE);

        Circle outline = new Circle();
        outline.setRadius(5);
        outline.setFill(Color.TRANSPARENT);
        outline.setStrokeWidth(1.0);
        outline.setStroke(Color.WHITE);

        

        planetMarker = new Group(point, outline);
        
        planetMarker.setTranslateX(adjustedPoint.getX());
        planetMarker.setTranslateY(adjustedPoint.getY());
        
        form.getChildren().add(planetMarker);

    }

    private void getOrbit(){
        Double a = reference.apogee;
        Double p = reference.perigee;

        Double eccentricity = (a - p) / (a + p);
        Double semimajor = (a + p) / 2;
        Double semiminor = semimajor * Math.sqrt(1 - eccentricity * eccentricity);

        Ellipse orbit = new Ellipse(0, 0, semimajor, semiminor);

        orbit.getTransforms().addAll(inclinationTransform, rotateTransform, yawTransform);

        //this bit is for putting the parent in the focus of the ellipse
        // Double focalLength = Math.sqrt(semimajor * semimajor - semiminor * semiminor);
        // form.setLayoutX(focalLength);

        this.orbit_ellipse = orbit;
        form.getChildren().addAll(orbit_ellipse);
    }
    
    public void set_parent(Feature parent){
        this.parent = parent;
    }

    public void setOrbitDegrees(double degrees){
        reference.angle = degrees;
        this.angle = reference.angle;
        render();
    }

    public void setSizeProportional(double pixels){
        double p2aRatio = perigee / apogee;
        double a2pRatio = apogee / perigee;
        reference.perigee = pixels * p2aRatio;
        reference.apogee = pixels * a2pRatio;
        render();
    }

    public void setInclinationDegrees(double degrees){
        this.inclination = degrees;
        inclinationTransform = new Rotate(inclination, 0, 0, 0, Rotate.Y_AXIS);
        render();
    }

    public void setYawDegree(double degrees){
        this.yaw = degrees;
        yawTransform = new Rotate(yaw, 0, 0, 0, Rotate.Z_AXIS);
        render();
    }

    public Ellipse getOrbitEllipse(){
        return this.orbit_ellipse;
    }

}
