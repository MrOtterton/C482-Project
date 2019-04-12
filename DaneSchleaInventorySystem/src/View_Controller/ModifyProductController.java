/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import static Model.Inventory.getAllPartsInventory;
import static Model.Inventory.getProductsInventory;
import Model.Part;
import Model.Product;
import static View_Controller.MainScreenController.productModifyIndex;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dane Schlea
 */
public class ModifyProductController implements Initializable {
    
    private ObservableList<Part> modCurrentParts = FXCollections.observableArrayList();
    
    @FXML
    private TextField modProductSearch;
    @FXML
    private TextField modProductIDField;
    @FXML
    private TextField modProductNameField;
    @FXML
    private TextField modProductInvField;
    @FXML
    private TextField modProductPriceField;
    @FXML
    private TextField modProductMaxField;
    @FXML
    private TextField modProductMinField;
    @FXML
    private TableView<Part> vModProductAdd;
    @FXML
    private TableColumn<Part, Integer> vModProductAddID;
    @FXML
    private TableColumn<Part, String> vModProductAddName;
    @FXML
    private TableColumn<Part, Integer> vModProductAddInv;
    @FXML
    private TableColumn<Part, Double> vModProductAddPrice;
    @FXML
    private TableView<Part> vModProductDelete;
    @FXML
    private TableColumn<Part, Integer> vModProductDeleteID;
    @FXML
    private TableColumn<Part, String> vModProductDeleteName;
    @FXML
    private TableColumn<Part, Integer> vModProductDeleteInv;
    @FXML
    private TableColumn<Part, Double> vModProductDeletePrice;
    
    private String exceptionMessage = new String();
    private int productID;
    private int productIndex = productModifyIndex();
    
    //Search bar and button
    @FXML
    void handleModProductSearch(ActionEvent event) {
        String term = modProductSearch.getText();
        ObservableList foundParts = Inventory.lookupPart(term);
        if(foundParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Part match found");
            alert.setHeaderText("No Part Names found matching " + term); 
            alert.showAndWait();
        } else {
            vModProductAdd.setItems(foundParts);
        }
}
    
    @FXML
    void handleModProductAddButton(ActionEvent event){
        Part part = vModProductAdd.getSelectionModel().getSelectedItem();
        modCurrentParts.add(part);
        updateModPartTableViewDelete();
    }
    
    @FXML
    void handleModProductDeleteButton(ActionEvent event){
        Part part = vModProductAdd.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete");
        alert.setHeaderText("Confirm delete");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            System.out.println("Deletion successful.");
            modCurrentParts.remove(part);
        }
        else{
            System.out.println("Operation cancelled.");
        }
    }
    
    @FXML
    void handleModProductSaveButton(ActionEvent event)throws IOException{
        String productName = modProductNameField.getText();
        String productInv = modProductInvField.getText();
        String productPrice = modProductPriceField.getText();
        String productMax = modProductMaxField.getText();
        String productMin = modProductMinField.getText();
        
        try{
            exceptionMessage = Product.isProductValid(productName, Double.parseDouble(productPrice), Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), modCurrentParts, exceptionMessage);
            if(exceptionMessage.length() > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error adding product.");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else{
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductParts(modCurrentParts);
                Inventory.updateProduct(productIndex, newProduct);
                
                Parent modProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error modifying product.");
            alert.setContentText("Form contains invalid fields.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleModProductCancel(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Parent modProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modProductCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else{
            System.out.println("Operation cancelled.");
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product product = getProductsInventory().get(productIndex);
        productID = getProductsInventory().get(productIndex).getProductID();
        
        modProductIDField.setText("Auto Gen: " + productID);
        modProductNameField.setText(product.getProductName());
        modProductInvField.setText(Integer.toString(product.getProductInStock()));
        modProductPriceField.setText(Double.toString(product.getProdcutPrice()));
        modProductMaxField.setText(Integer.toString(product.getProductMax()));
        modProductMinField.setText(Integer.toString(product.getProductMin()));
        modCurrentParts = product.getProductParts();
        
        vModProductAddID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        vModProductAddName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        vModProductAddInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        vModProductAddPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        vModProductDeleteID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        vModProductDeleteName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        vModProductDeleteInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        vModProductDeletePrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        
        updateModPartTableViewAdd();
        updateModPartTableViewDelete();
    }    

    public void updateModPartTableViewDelete() {
        vModProductAdd.setItems(getAllPartsInventory());
    }

    public void updateModPartTableViewAdd() {
        vModProductDelete.setItems(modCurrentParts);
    }
}
