// package com.sysedit;

// import java.io.IOException;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.geometry.Bounds;
// import javafx.geometry.HPos;
// import javafx.geometry.Point2D;
// import javafx.geometry.VPos;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.Slider;
// import javafx.scene.effect.Light.Point;
// import javafx.scene.input.MouseButton;
// import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Rectangle;
// import javafx.stage.Stage;

// public class OrbitManager {
//     Orbit referenceOrbit;
//     Feature reference;
//     private boolean isManagerVisible = false;
//     private boolean isButtonVisible = false;
//     private Sim sim = Sim.getSim();
//     private StackPane stacky;
//     private Rectangle orbitManagingButton;
//     private OrbitEditorController controller;
//     private Stage orbitStage;
    
//     public OrbitManager(Feature f, Orbit o){
//         this.referenceOrbit = o;
//         this.reference = f;
//         this.orbitManagingButton = referenceOrbit.orbitManagingButton;
//         orbitManagingButton.setFill(Color.WHITE);

//         imbueButtonManaging();
//     }

//     public void setButtonVisible(boolean b){
//         if(b){
//             referenceOrbit.form.getChildren().add(orbitManagingButton);

//             Bounds boundr = referenceOrbit.getOrbitEllipse().getBoundsInParent();
//             Point2D p = new Point2D(boundr.getMaxX(), boundr.getMaxY());
//             orbitManagingButton.setTranslateX(p.getX());
//             orbitManagingButton.setTranslateY(p.getY());

//             isButtonVisible = true;
//         } 
//         if(isButtonVisible){
//             if(!b){
//                 referenceOrbit.form.getChildren().remove(orbitManagingButton);
//                 isButtonVisible = false;
//             }
//         }
//     }

//     public void setManagerVisible() throws IOException{
//         if(controller == null){
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("orbitEditor.fxml"));
//             orbitStage = loader.load();
//             controller = loader.getController();
//             controller.setUp(reference, referenceOrbit);
//         }
//         isManagerVisible = true;
//         orbitStage.show();
//     }

//     public void setManagerInvisible(){
//         isManagerVisible = false;
//         orbitStage.hide();
//     }

//     private void imbueButtonManaging(){
//         this.orbitManagingButton.setOnMouseClicked(e->{
//             try {
//                 setManagerVisible();
//             } catch (IOException e1) {
//                 // TODO Auto-generated catch block
//                 e1.printStackTrace();
//             }
//         });
//     }


// }
