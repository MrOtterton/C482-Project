/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dane Schlea
 */
public class AddPartController implements Initializable {
    
    //Radio button and text fields
    @FXML
    private RadioButton radioInHouse;
    @FXML
    private RadioButton radioOutsourced;
    @FXML
    private TextField partIDField;
    @FXML
    private TextField partNameField;
    @FXML
    private TextField partInvField;
    @FXML
    private TextField partPriceField;
    @FXML
    private TextField partMaxField;
    @FXML
    private TextField partMinField;
    @FXML
    private Label dynLabel;
    @FXML
    private TextField dynTextField;
   
    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partID;
    
    //Radio buttons and dynamic fields
    @FXML
    void addPartInHouseRadio(ActionEvent event){
        isOutsourced = false;
        dynLabel.setText("Machine ID");
        dynTextField.setPromptText("Machine ID");
        radioOutsourced.setSelected(false);
    }
    
    @FXML
    void addPartOutsourcedRadio(ActionEvent event){
        isOutsourced = true;
        dynLabel.setText("Company Name");
        dynTextField.setPromptText("Company Name");
        radioInHouse.setSelected(true);
    }
    
    //save and cancel buttons
    @FXML
    void handleAddPartSaveButton(ActionEvent event) throws IOException{
        String partName = partNameField.getText();
        String partInv = partInvField.getText();
        String partPrice = partPriceField.getText();
        String partMax = partMaxField.getText();
        String partMin = partMinField.getText();
        String partDyn = dynTextField.getText();
        
        try{
            exceptionMessage = Part.isPartValid(partName, Double.parseDouble(partPrice), Integer.parseInt(partInv), Integer.parseInt(partMin), Integer.parseInt(partMax), exceptionMessage);
            if(exceptionMessage.length() > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error adding part.");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else{
                if(isOutsourced == false){
                    System.out.println("Part name: " + partName);
                    InhousePart inPart = new InhousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setMachineID(Integer.parseInt(partDyn));
                    Inventory.addPart(inPart);
                    }
                else{
                    OutsourcedPart outPart = new OutsourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setCompanyName(partDyn);
                    Inventory.addPart(outPart);
                }
                Parent partSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(partSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error adding part");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains invalid fields.");
            alert.showAndWait();
        }
    }
    
    @FXML
    void handleAddPartCancelButton(ActionEvent event) throws IOException{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to delete part " + partNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Parent partCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(partCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else{
            System.out.println("Cancelled.");
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        partIDField.setText("Auto Gen: " + partID);
    }    
    
}
