package com.sysedit;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class Rightclickcontrol {
    //this is the right click controller. Wow shocking
    Sim sim = Sim.getSim();
    Feature reference;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private MenuItem addSatButton;

    @FXML
    void Paste(ActionEvent event) {

    }

    @FXML
    void editDetails(ActionEvent event) throws IOException {
        sim.open_editor(reference, false);
    }

    @FXML
    void addMega(ActionEvent event) {

    }

    @FXML
    void addRing(ActionEvent event) {

    }

    @FXML
    void addRingSys(ActionEvent event) {

    }

    @FXML
    void addSatellite(ActionEvent event) {
        sim.create_satellite(reference);
    }

    @FXML
    void changeParent(ActionEvent event) {

    }

    @FXML
    void copy(ActionEvent event) {

    }

    @FXML
    void copyAll(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void toggleOrbit(ActionEvent event) {
        reference.show_orbit = !reference.show_orbit;
        sim.updateScene();
    }

    @FXML
    void toggleSystem(ActionEvent event) {
        reference.is_expanded = !reference.is_expanded;
        reference.form.getChildren().clear();
        sim.updateScene();
    }

    public void set_reference(Feature f){
        this.reference = f;
    }

    public void enableAddingSatellites(){
        addSatButton.setDisable(false);
    }

}
