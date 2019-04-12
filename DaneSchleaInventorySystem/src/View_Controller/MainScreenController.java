/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InhousePart;
import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import Model.Inventory;
import static Model.Inventory.getAllPartsInventory;
import static Model.Inventory.getPartIDCount;
import static Model.Inventory.getProductsInventory;
import static Model.Inventory.removePart;
import static Model.Inventory.removeProduct;
import static Model.Inventory.validateRemovePart;
import static Model.Inventory.validateRemoveProduct;
import Model.OutsourcedPart;
import Model.Part;
import Model.Product;
import invsys.DSIS;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;


/**
 * FXML Controller class
 *
 * @author Dane Schlea
 */
public class MainScreenController implements Initializable {
    
    //View for parts
    @FXML
    private TableView<Part> vParts;
    @FXML
    private TableColumn<Part, Integer> vPartsIDColumn;
    @FXML
    private TableColumn<Part, String> vPartsNameColumn;
    @FXML
    private TableColumn<Part, Integer> vPartsInvColumn;
    @FXML
    private TableColumn<Part, Double> vPartsPriceColumn;
    
    //View for products
    @FXML
    private TableView<Product> vProducts;
    @FXML
    private TableColumn<Product, Integer> vProductsIDColumn;
    @FXML
    private TableColumn<Product, String> vProductsNameColumn;
    @FXML
    private TableColumn<Product, Integer> vProductsInvColumn;
    @FXML
    private TableColumn<Product, Double> vProductsPriceColumn;
    
    //Search bar
    @FXML
    private TextField txtSearchParts;
    
    @FXML
    private TextField txtSearchProducts;
    
    //Part and Product modifiers
    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partModifyIndex() {
        return modifyPartIndex;
    }

    public static int productModifyIndex() {
        return modifyProductIndex;
    }
    
    public MainScreenController(){
    }
    
    //Buttons
    @FXML
    void partSearchHandler(ActionEvent event) {
        String term = txtSearchParts.getText();
        ObservableList foundParts = Inventory.lookupPart(term);
        if(foundParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Part match found");
            alert.setHeaderText("No Part Names found matching " + term); 
            alert.showAndWait();
        } else {
            vParts.setItems(foundParts);
        }
    }

    @FXML
    void productSearchHandler(ActionEvent event) {
        String term = txtSearchProducts.getText();
        ObservableList foundProducts = Inventory.lookupProduct(term);
        if(foundProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Product match found");
            alert.setHeaderText("No Product Names found matching " + term); 
            alert.showAndWait();
        } else {
            vProducts.setItems(foundProducts);
        }
}
    
    @FXML
    void exitHandler(ActionEvent event) throws IOException{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm exit.");
        alert.setHeaderText("Confirm exit.");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            System.exit(0);
        }
    }
    
    @FXML
    void addPartHandler(ActionEvent event) throws IOException{
        showAddPartWindow(event);
    }
    
    @FXML
    void modifyPartHandler(ActionEvent event) throws IOException{
        showModifyPartWindow(event);
    }
    
    @FXML
    void deletePartHandler(ActionEvent event) throws IOException{
        Part part = vParts.getSelectionModel().getSelectedItem();
        if(validateRemovePart(part)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Part delete error!");
            alert.setHeaderText("Part cannot be removed.");
            alert.setContentText("This part is used in a product.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part delete");
            alert.setHeaderText("Confirm delete?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                removePart(part);
                updatePartTable();
                System.out.println("Part " + part.getPartName() + " was removed.");
            }
            else{
                System.out.println("Part " + part.getPartName() + " was not removed.");
            }
        }
    }
    
    @FXML
    void addProductHandler(ActionEvent event) throws IOException{
        showAddProductWindow(event);
    }
    
    @FXML
    void modifyProductHandler(ActionEvent event) throws IOException{
        showModifyProductWindow(event);
    }
    
    @FXML
    void deleteProductHandler(ActionEvent event) throws IOException{
        Product product = vProducts.getSelectionModel().getSelectedItem();
        if(validateRemoveProduct(product)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Product delete error!");
            alert.setHeaderText("Product cannot be removed.");
            alert.setContentText("This product contains a part.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product delete");
            alert.setHeaderText("Confirm delete?");
            alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                removeProduct(product);
                updateProductTable();
                System.out.println("Part " + product.getProductName() + " was removed.");
            }
            else{
                System.out.println("Part " + product.getProductName() + " was not removed.");
            }
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vPartsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        vPartsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        vPartsInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        vPartsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        
        vProductsIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        vProductsNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        vProductsInvColumn.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        vProductsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        
        updatePartTable();
        updateProductTable();
    }    

    
    //Show windows and update the TableViews
    public void updatePartTable() {
        vParts.setItems(getAllPartsInventory());
    }

    private void showModifyPartWindow(ActionEvent event) throws IOException {
        modifyPart = vParts.getSelectionModel().getSelectedItem();
        modifyPartIndex = getAllPartsInventory().indexOf(modifyPart);
        Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
        Scene modifyPartScene = new Scene(modifyPartParent);
        Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyPartStage.setScene(modifyPartScene);
        modifyPartStage.show();
    }

    private void showAddPartWindow(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    public void updateProductTable() {
        vProducts.setItems(getProductsInventory());
    }

    private void showModifyProductWindow(ActionEvent event) throws IOException {
        modifyProduct = vProducts.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductsInventory().indexOf(modifyProduct);
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProductParent);
        Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyProductStage.setScene(modifyProductScene);
        modifyProductStage.show();
    }

    private void showAddProductWindow(ActionEvent event) throws IOException {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    public void setMainApp(DSIS mainApp) {
        updatePartTable();
        updateProductTable();
    }
    
    private void baseParts(){
        int partID = Inventory.getPartIDCount();
        Inventory.addPart(new InhousePart(partID, "Engine", 234.56, 7, 1, 10, 45));
        Inventory.addPart(new OutsourcedPart(getPartIDCount(), "Oil", 44.55, 5, 1, 10, "Oil Co"));  
    }
    
    private void baseProducts(){
        int productID = Inventory.getProductIDCount();
        ObservableList<Part> noParts = FXCollections.observableArrayList();
        Product firstProduct = new Product(productID, "No Parts", 999.99, 6, 1, 10);
        firstProduct.setProductParts(noParts);
        Inventory.addProduct(firstProduct);
        
        ObservableList<Part> checkPart = Inventory.lookupPart("Engine");
        Product secondProduct = new Product(Inventory.getProductIDCount(), "Piston", 333.22, 10, 1, 10);
        secondProduct.setProductParts(checkPart);
        Inventory.addProduct(secondProduct);
    }
    
    public void setBaseTV(){
        baseParts();
        baseProducts();
    }
    
}
