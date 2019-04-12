/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import static Model.Inventory.getAllPartsInventory;
import Model.OutsourcedPart;
import Model.Part;
import static View_Controller.MainScreenController.partModifyIndex;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dane Schlea
 */
public class ModifyPartController implements Initializable {
    
    //Radio button and text fields
    @FXML
    private TextField modPartIDField;
    @FXML
    private TextField modPartNameField;
    @FXML
    private TextField modPartInvField;
    @FXML
    private TextField modPartPriceField;
    @FXML
    private TextField modPartMaxField;
    @FXML
    private TextField modPartMinField;
    @FXML
    private TextField modDynField;
    @FXML
    private Label modDynLabel;
    @FXML
    private RadioButton modInHouseRadio;
    @FXML
    private RadioButton modOutsourcedRadio;
    
    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partID;
    int partIndex = partModifyIndex();
    
    //Radio Buttons and dynamic fields
    @FXML
    void modPartInHouseRadio(ActionEvent event) throws IOException{
        isOutsourced = false;
        modOutsourcedRadio.setSelected(false);
        modDynLabel.setText("Machine ID");
        modDynField.setText("");
        modDynField.setPromptText("Machine ID");
    }
    
    @FXML
    void modPartOutsourcedRadio(ActionEvent event) throws IOException{
        isOutsourced = true;
        modInHouseRadio.setSelected(false);
        modDynLabel.setText("Company Name");
        modDynField.setText("");
        modDynField.setPromptText("Company Name");
    }
    
    //save and cancel buttons
    @FXML
    void handleModPartSaveButton(ActionEvent event) throws IOException{
        String partName = modPartNameField.getText();
        String partInv = modPartInvField.getText();
        String partPrice = modPartPriceField.getText();
        String partMax = modPartMaxField.getText();
        String partMin = modPartMinField.getText();
        String partDyn = modDynField.getText();
        
        try{
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), (int) Double.parseDouble(partPrice), exceptionMessage);
            if(exceptionMessage.length() > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error modifying part.");
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
                    Inventory.updatePart(partIndex, inPart);
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
                    Inventory.updatePart(partIndex, outPart);
                }
                Parent partSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(partSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error adding part");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains invalid fields.");
            alert.showAndWait();
        }
    }
    
    @FXML
    void handleModPartCancelButton(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Cancel Modify");
        alert.setContentText("Are you sure you want to cancel modification?");
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
        Part part = getAllPartsInventory().get(partIndex);
        partID = getAllPartsInventory().get(partIndex).getPartID();
        modPartIDField.setText("Auto Gen: " + partID);
        modPartNameField.setText(part.getPartName());
        modPartInvField.setText(Integer.toString(part.getPartInStock()));
        modPartPriceField.setText(Double.toString(part.getPartPrice()));
        modPartMaxField.setText(Integer.toString(part.getPartMax()));
        modPartMinField.setText(Integer.toString(part.getPartMin()));
        if(part instanceof InhousePart){
            modDynLabel.setText("Machine ID");
            modDynField.setText(Integer.toString(((InhousePart) getAllPartsInventory().get(partIndex)).getMachineID()));
            modInHouseRadio.setSelected(true);
        }
        else{
            modDynLabel.setText("Company Name");
            modDynField.setText(((OutsourcedPart) getAllPartsInventory().get(partIndex)).getCompanyName());
            modOutsourcedRadio.setSelected(true);
        }
    }    
    
}
