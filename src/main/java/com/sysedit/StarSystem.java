package com.sysedit;

import java.util.ArrayList;

import javafx.scene.Group;

public class StarSystem {
    private ArrayList<Feature> features = new ArrayList<>();
    private Feature parent;

    StarSystem(){

    }

    void add_feature(Feature new_feature){
        features.add(new_feature);
    }

    public ArrayList<Feature> get_features(){
        return features;
    }

    public void removeFeature(Feature f){
        features.remove(f);
    }

    public Group render(){
        Group g = new Group();
        for (Feature f: features){
            g.getChildren().addAll(f.render());
        }
        return g;
    }
}
