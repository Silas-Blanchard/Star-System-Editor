package com.sysedit;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Node;

public class StarSystem {
    public ArrayList<Feature> features = new ArrayList<>();
    public Group subgroup = new Group();
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
        // for (Feature f: features){
        //     f.render();
        // }
    }

    public void setup_rendering(Group subsubgroup){
        //adds the group to its list to keep track of them. Adds the group to sim so it appears.
        objects.add(subsubgroup);
        subgroup.getChildren().add(subsubgroup);
    }

    public void remove_rendering(Feature f){
        objects.remove(f.form);
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

    public void addRendering(Feature f){
        objects.add(f.form);
        subgroup.getChildren().add(f.form);
    }

    public void deleteFeatureRendering(){
        sim.remove_node(subgroup);
    }
}
