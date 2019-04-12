/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Dane Schlea
 */
public class Product {
    
    //Array and product identifiers
    private static ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;
    
    //Constructor
    public Product(){
        productID = new SimpleIntegerProperty(0);
        name = new SimpleStringProperty("");
        price = new SimpleDoubleProperty(0);
        inStock = new SimpleIntegerProperty(0);
        min = new SimpleIntegerProperty(0);
        max = new SimpleIntegerProperty(0);
    }

    public Product(int productID, String name, double price, int inStock, int min, int max) {
        this.productID = new SimpleIntegerProperty(productID);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.inStock = new SimpleIntegerProperty(inStock);
        this.min = new SimpleIntegerProperty(min);
        this.max = new SimpleIntegerProperty(max);
    }
    
    //Getters
    public IntegerProperty productIDProperty() {
        return productID;
    }

    public StringProperty productNameProperty() {
        return name;
    }

    public DoubleProperty productPriceProperty() {
        return price;
    }

    public IntegerProperty productInvProperty() {
        return inStock;
    }
    
    public int getProductID() {
        return this.productID.get();
    }

    public String getProductName() {
        return this.name.get();
    }

    public double getProdcutPrice() {
        return this.price.get();
    }

    public int getProductInStock() {
        return this.inStock.get();
    }

    public int getProductMin() {
        return this.min.get();
    }

    public int getProductMax() {
        return this.max.get();
    }
    
    public ObservableList<Part> getProductParts() {
        return associatedParts;
    }
    
    //Setters

    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public void setProductName(String name) {
        this.name.set(name);
    }

    public void setProductPrice(double price) {
        this.price.set(price);
    }

    public void setProductInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setProductMin(int min) {
        this.min.set(min);
    }

    public void setProductMax(int max) {
        this.max.set(max);
    }
    
    public void setProductParts(ObservableList<Part> associatedParts){
        this.associatedParts = associatedParts;
    }
    
    //Add, remove, lookup for associatedParts
    public void addAssociatedPart(Part associatedPart){
        this.associatedParts.add(associatedPart);
    }
    
    public boolean removeAssociatedPart(int partID){
        for(Part p : associatedParts){
            if(p.getPartID() == partID){
                associatedParts.remove(p);
                return true;
            }
        }
        return false;
    }
    
    public Part lookupAssociatedPart(int partID){
        for(Part p : associatedParts){
            if(p.getPartID() == partID){
                return p;
            }
        }
        return null;
    }
    
    //Validation
    public static String isProductValid(String name, double price, int inv, int min, int max, ObservableList<Part> associatedParts, String errorMessage){
        double partsSum = 0.00;
        for(int i = 0; i <associatedParts.size(); i++){
            partsSum = partsSum + associatedParts.get(i).getPartPrice();
        }
        if(name == null){
            errorMessage = errorMessage + "Must enter a name.";
        }
        else if(price <= 0){
            errorMessage = errorMessage + "Price must be greater than $0.";
        }
        else if(price < partsSum){
            errorMessage = errorMessage + "Product price must be greater than sum of the parts price.";
        }
        else if(associatedParts.size() < 1){
            errorMessage = errorMessage + "Product must contain at least 1 part.";
        }
        else if(inv < 1){
            errorMessage = errorMessage + "Inventory must be greater than 0.";
        }
        else if(max < min){
            errorMessage = errorMessage + "Inventory MIN must be less than the MAX.";
        }
        else if(inv < min || max > inv){
            errorMessage = errorMessage + "Inventory must be between MIN and MAX values.";
        }
        return errorMessage;
    }
}
