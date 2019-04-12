/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.*;

/**
 *
 * @author Dane Schlea
 */
public abstract class Part {

   //Product identifiers
    protected IntegerProperty partID;
    protected StringProperty name;
    protected DoubleProperty price;
    protected IntegerProperty inStock;
    protected IntegerProperty min;
    protected IntegerProperty max;
   
   //Constructor
    public Part() {
    partID = new SimpleIntegerProperty();
    name = new SimpleStringProperty();
    price = new SimpleDoubleProperty();
    inStock = new SimpleIntegerProperty();
    min = new SimpleIntegerProperty();
    max = new SimpleIntegerProperty();
}
   
   //Getters
   public IntegerProperty partIDProperty() {
        return partID;
    }

    public StringProperty partNameProperty() {
        return name;
    }

    public DoubleProperty partPriceProperty() {
        return price;
    }

    public IntegerProperty partInvProperty() {
        return inStock;
    }
   
    public int getPartID() {
        return this.partID.get();
    }

    public String getPartName() {
        return this.name.get();
    }

    public double getPartPrice() {
        return this.price.get();
    }

    public int getPartInStock() {
        return this.inStock.get();
    }

    public int getPartMin() {
        return this.min.get();
    }

    public int getPartMax() {
        return this.max.get();
    }
   
    
    //Setter

    public void setPartID(int partID) {
        this.partID.set(partID);
    }

    public void setPartName(String name) {
        this.name.set(name);
    }

    public void setPartPrice(double price) {
        this.price.set(price);
    }

    public void setPartInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setPartMin(int min) {
        this.min.set(min);
    }

    public void setPartMax(int max) {
        this.max.set(max);
    }
    
    //validation for identifiers
    public static String isPartValid(String name, double price, int inv, int min, int max, String errorMessage){
        if(name == null){
            errorMessage = errorMessage + "Must enter a name.";
        }
        else if(price <= 0){
            errorMessage = errorMessage + "Price must be greater than $0.";
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
