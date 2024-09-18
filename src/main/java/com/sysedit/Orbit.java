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
    // public World reference;

    private Double planet_x;
    private Double planet_y;

    private Feature parent;
    public Rotate rotateTransform;
    public Rotate inclinationTransform;
    public Rotate yawTransform;

    public Group form = new Group();

    private Group planetMarker;
    private Ellipse orbit_ellipse;

    //public OrbitManager orbitManagr;
    public Rectangle orbitManagingButton = new Rectangle(10, 10);

    public Point2D planetPoint = new Point2D(0,0);

    // Orbit(World reference){
    //     //defaults
    //     this.angle = 0.0;
    //     this.inclination = 0.0;
    //     this.yaw = 0.0;
    //     this.apogee = reference.apogee;
    //     this.perigee = reference.perigee;

    //     this.parent = reference.parent;

    //     reference.system.setup_rendering(form);

    //     rotateTransform = new Rotate(75, 0, 0, 0, Rotate.X_AXIS);
    //     inclinationTransform = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    //     yawTransform = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    //     orbitManagr = new OrbitManager(reference, this);
    // }

    Orbit(){
        this.angle = 0.0;
        this.inclination = 0.0;
        this.yaw = 0.0;
        this.apogee = 100.0;
        this.perigee = 100.0;

        rotateTransform = new Rotate(75, 0, 0, 0, Rotate.X_AXIS);
        inclinationTransform = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        yawTransform = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
    }

    public Group getForm(Circle body){
        Group g = new Group();
        
        //Phase 1, finding the shape of the orbit
        Ellipse orbit = getOrbit();
        orbit.setStrokeWidth(2.0);
        orbit.setStroke(Color.WHITE);
        orbit.setFill(Color.TRANSPARENT);
        orbit.setMouseTransparent(true);

        //Phase 2, Moving the "body" of our planet to its proper spot
        Group planetMarker = renderPlanet(orbit, body);

        body.setTranslateX(planetPoint.getX());
        body.setTranslateY(planetPoint.getY());


        g.getChildren().addAll(orbit, planetMarker, body);
        return g;
    }

    private Group renderPlanet(Ellipse orbit, Circle body){
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

        //after all of that, this is just the objective point on the screen
        this.planetPoint = orbit.localToParent(planet_x, planet_y);

        //making the planet position marker
        Circle point = new Circle();
        point.setRadius(1);
        point.setFill(Color.WHITE);

        Circle outline = new Circle();
        outline.setRadius(5);
        outline.setFill(Color.TRANSPARENT);
        outline.setStrokeWidth(1.0);
        outline.setStroke(Color.WHITE);

        planetMarker = new Group(point, outline);
        
        planetMarker.setTranslateX(planetPoint.getX());
        planetMarker.setTranslateY(planetPoint.getY());

        return planetMarker;
    }

    private Ellipse getOrbit(){
        Double a = this.apogee;
        Double p = this.perigee;

        Double eccentricity = (a - p) / (a + p);
        Double semimajor = (a + p) / 2;
        Double semiminor = semimajor * Math.sqrt(1 - eccentricity * eccentricity);

        Ellipse orbit = new Ellipse(0, 0, semimajor, semiminor);

        orbit.getTransforms().addAll(inclinationTransform, rotateTransform, yawTransform);

        //this bit is for putting the parent in the focus of the ellipse
        // Double focalLength = Math.sqrt(semimajor * semimajor - semiminor * semiminor);
        // form.setLayoutX(focalLength);

        this.orbit_ellipse = orbit;
        return orbit_ellipse;
    // }
    
    // public void set_parent(Feature parent){
    //     this.parent = parent;
    // }

    // public void setOrbitDegrees(double degrees){
    //     reference.angle = degrees;
    //     this.angle = reference.angle;
    // }

    // public void setSizeProportional(double pixels){
    //     double p2aRatio = perigee / apogee;
    //     double a2pRatio = apogee / perigee;
    //     reference.perigee = pixels * p2aRatio;
    //     reference.apogee = pixels * a2pRatio;
    // }

    // public void setInclinationDegrees(double degrees){
    //     this.inclination = degrees;
    //     inclinationTransform = new Rotate(inclination, 0, 0, 0, Rotate.Y_AXIS);
    // }

    // public void setYawDegree(double degrees){
    //     this.yaw = degrees;
    //     yawTransform = new Rotate(yaw, 0, 0, 0, Rotate.Z_AXIS);
    // }

    // public Ellipse getOrbitEllipse(){
    //     return this.orbit_ellipse;
    // }
    }
}
