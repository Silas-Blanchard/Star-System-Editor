package com.sysedit;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditorController {
    Feature reference;
    Sim sim = Sim.getSim();

    @FXML
    private ButtonBar bar;

    @FXML
    private Button cancel;

    @FXML
    private Label degreeLabel;

    @FXML
    private TextField editApogee;

    @FXML
    private Slider editDegree;

    @FXML
    private TextField editInclination;

    @FXML
    private TextField editName;

    @FXML
    private TextField editPerigee;

    @FXML
    private TextField editRadius;

    @FXML
    private Button submit;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleActionEvent(ActionEvent event) {

    }

    @FXML
    void submit(ActionEvent event) throws IOException {
        try{
            reference.name = editName.getText();
            reference.angle = editDegree.getValue();

            Double apogee = Double.parseDouble(editApogee.getText());
            Double perigee = Double.parseDouble(editPerigee.getText());
            Double radius = Double.parseDouble(editRadius.getText());
            Double inclination = Double.parseDouble(editInclination.getText());

            reference.apogee = apogee;
            reference. perigee = perigee;
            reference.radius = radius;
            reference.inclination = inclination;
        } catch (Exception e){
            sim.open_editor(reference, true);
        } finally{
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        }
        //sim.updateScene();
    }

    public void set_values(Feature f){ //sets values for the editor menu
        editName.setText(f.name);
        editApogee.setText(f.apogee.toString());
        editPerigee.setText(f.perigee.toString());
        editRadius.setText(f.radius.toString());

        editDegree.setValue(f.angle);
        degreeLabel.textProperty().bind(Bindings.format("%.2f", editDegree.valueProperty()));

        editInclination.setText(f.inclination.toString());

        reference = f;
    }
}
