/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Dane Schlea
 */
public class Inventory {
    
    //Different inventories
    
    public static ObservableList<Product> products = FXCollections.observableArrayList();
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    public static int partIDCount = 0;
    public static int productIDCount = 0;
    
    //Product inventory
    public static int getProductIDCount(){
        productIDCount++;
        return productIDCount;
    }
    public static ObservableList<Product> getProductsInventory(){
        return products;
    }
    
    public static void addProduct(Product product){
        products.add(product);
    }
    
    public static void removeProduct(Product product){
        products.remove(product);
    }
    
    public static boolean validateRemoveProduct(Product product){
        return product.getProductParts() == null;
    }
    
    public static ObservableList lookupProduct(String searchTerm) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        
        if(searchTerm.length() == 0) {
            foundProducts = products;
        } else {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getProductName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundProducts.add(products.get(i));
                }
            }    
        }
        return foundProducts;
    }
    public static void updateProduct(int indexNo, Product product){
        products.set(indexNo, product);
    }
    
    //Part inventory
    
    public static int getPartIDCount(){
        partIDCount++;
        return partIDCount;
    }
    
    public static ObservableList<Part> getAllPartsInventory(){
        return allParts;
    }
    
    public static void addPart(Part part){
        allParts.add(part);
    }
   
    //Was labeled as deletePart but renamed to removePart for consistency
    public static void removePart(Part part){
        allParts.remove(part);
    }
    
    public static boolean validateRemovePart(Part part){
        boolean inProduct = false;
        for (Product product : products) {
            if(product.getProductParts().contains(part)){
                inProduct = true;
            }
        }
        return inProduct;
    }
    
    public static ObservableList lookupPart(String searchTerm) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        
        if(searchTerm.length() == 0) {
            foundParts = allParts;//return all if blank as passed in as search term
        } else {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getPartName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundParts.add(allParts.get(i));
                }
            }    
        }
        
        return foundParts;
}
    
    public static void updatePart(int indexNo, Part part){
        allParts.set(indexNo, part);
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
