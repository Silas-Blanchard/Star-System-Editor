package com.sysedit;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Shape;

public class StarSystem {
    private ArrayList<Feature> features = new ArrayList<>();
    public Group subgroup = new Group();
    private Feature parent;
    public ArrayList<Node> objects;
    Sim sim;

    StarSystem(){
        sim = Sim.getSim();
        objects = new ArrayList<Node>();
        sim.add_node(subgroup);
    }

    public void add_feature(Feature new_feature){
        features.add(new_feature);
    }

    public ArrayList<Feature> get_features(){
        return features;
    }

    public void removeFeature(Feature f){
        features.remove(f);
    }

    public void render(){
        for (Feature f: features){
            f.render();
        }
    }

    public void setup_rendering(Group subsubgroup){
        //adds the group to its list to keep track of them. Adds the group to sim so it appears.
        objects.add(subsubgroup);
        subgroup.getChildren().add(subsubgroup);
    }

    public void remove_rendering(Group group_to_remove){
        objects.remove(group_to_remove);
    }

    public void remove_system(){
        sim.get_the_group().getChildren().remove(subgroup);
    }

    public void set_parent(Feature newParent){
        for (Node n : objects){
            newParent.system.objects.add(n);
            newParent.system.subgroup.getChildren().add(n);
        }
    }
}
