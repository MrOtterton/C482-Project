/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import static Model.Inventory.getAllPartsInventory;
import Model.Part;
import Model.Product;
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
public class AddProductController implements Initializable {
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    
    @FXML
    private TextField productSearch;
    @FXML
    private TextField productIDField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productInvField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productMaxField;
    @FXML
    private TextField productMinField;
    @FXML
    private TableView<Part> vProductAdd;
    @FXML
    private TableColumn<Part, Integer> vProductAddID;
    @FXML
    private TableColumn<Part, String> vProductAddName;
    @FXML
    private TableColumn<Part, Integer> vProductAddInv;
    @FXML
    private TableColumn<Part, Double> vProductAddPrice;
    @FXML
    private TableView<Part> vProductDelete;
    @FXML
    private TableColumn<Part, Integer> vProductDeleteID;
    @FXML
    private TableColumn<Part, String> vProductDeleteName;
    @FXML
    private TableColumn<Part, Integer> vProductDeleteInv;
    @FXML
    private TableColumn<Part, Double> vProductDeletePrice;
    
    private String exceptionMessage = new String();
    private int productID;
    
    //Search bar and button
    @FXML
    void handleProductSearch(ActionEvent event) {
        String term = productSearch.getText();
        ObservableList foundParts = Inventory.lookupPart(term);
        if(foundParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Part match found");
            alert.setHeaderText("No Part Names found matching " + term); 
            alert.showAndWait();
        } else {
            vProductAdd.setItems(foundParts);
        }
}
    
    @FXML
    void handleProductAddButton(ActionEvent event){
        Part part = vProductAdd.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updatePartTableViewDelete();
    }
    
    @FXML
    void handleAddProductDeleteButton(ActionEvent event){
        Part part = vProductAdd.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete");
        alert.setHeaderText("Confirm delete");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            System.out.println("Deletion successful.");
            currentParts.remove(part);
        }
        else{
            System.out.println("Operation cancelled.");
        }
    }
    
    @FXML
    void handleAddProductSaveButton(ActionEvent event)throws IOException{
        String productName = productNameField.getText();
        String productInv = productInvField.getText();
        String productPrice = productPriceField.getText();
        String productMax = productMaxField.getText();
        String productMin = productMinField.getText();
        
        try{
            exceptionMessage = Product.isProductValid(productName, Double.parseDouble(productPrice), Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), currentParts, exceptionMessage);
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
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);
                
                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding product.");
            alert.setContentText("Form contains invalid fields.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleAddProductCancel(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
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
        vProductAddID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        vProductAddName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        vProductAddInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        vProductAddPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        vProductDeleteID.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        vProductDeleteName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        vProductDeleteInv.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        vProductDeletePrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updatePartTableViewAdd();
        updatePartTableViewDelete();
        productID = Inventory.getPartIDCount();
        productIDField.setText("Auto Gen: " + productID);
    }    

    public void updatePartTableViewDelete() {
        vProductAdd.setItems(getAllPartsInventory());
    }

    public void updatePartTableViewAdd() {
        vProductDelete.setItems(currentParts);
    }
    
}
