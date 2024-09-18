// package com.sysedit;

// import javafx.beans.binding.Bindings;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.Slider;
// import javafx.stage.Stage;

// public class OrbitEditorController {
//     Feature reference;
//     Orbit referenceOrbit;

//     @FXML
//     private Button Button;

//     @FXML
//     private Label InclinationLabel;

//     @FXML
//     private Slider InclinationScroller;

//     @FXML
//     private Label SizeLabel;

//     @FXML
//     private Slider SizeScroller;

//     @FXML
//     private Label YawLabel;

//     @FXML
//     private Slider YawScroller;

//     @FXML
//     void closeEditor(ActionEvent event) {
//         Stage stage = (Stage) Button.getScene().getWindow();
//         stage.close();
//     }

//     public void setUp(Feature f, Orbit o){
//         this.reference = f;
//         this.referenceOrbit = o;

//         InclinationScroller.setValue(referenceOrbit.inclination);
//         SizeScroller.setValue(referenceOrbit.perigee);
//         YawScroller.setValue(referenceOrbit.yaw);

//         InclinationScroller.valueProperty().addListener((observable, oldValue, newValue) -> {
//             referenceOrbit.setInclinationDegrees(newValue.doubleValue());
//         });

//         SizeScroller.valueProperty().addListener((observable, oldValue, newValue) -> {
//             referenceOrbit.setSizeProportional(newValue.doubleValue());
//         });

//         YawScroller.valueProperty().addListener((observable, oldValue, newValue) -> {
//             referenceOrbit.setYawDegree(newValue.doubleValue());
//         });

//     }
// }
