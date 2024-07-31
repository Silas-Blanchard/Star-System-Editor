package com.sysedit;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class BackgroundClickControl {

    Sim sim = Sim.getSim();

    @FXML
    void paste(ActionEvent event) {

    }

    @FXML
    void newCenter(ActionEvent event) {
        sim.set_new_parent();
    }



}
