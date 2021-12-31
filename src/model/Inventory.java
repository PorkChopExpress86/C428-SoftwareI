package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Blake Bowden
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart new part object to be added to allParts arrayList
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct new product object to be added to allProducts arrayList
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * @param partId id of the part object that is to be matched
     * @return the part object that has a matching id to param partId
     */
    public static Part lookupPart(int partId) {
        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * @param productId id of the product object that is to be matched
     * @return the product object that has a matching id to param productId
     */
    public static Product lookupProduct(int productId) {
        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * @param partName a string that is the search parameter
     * @return a list of the parts that contain the partName
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();

        partName = partName.toLowerCase();

        for (Part part : Inventory.getAllParts()) {
            if (part.getName().toLowerCase().contains(partName)) {
                matchedParts.add(part);
            }
        }
        return matchedParts;
    }

    /**
     * @param productName a string that is the search parameter
     * @return a list of the products that contain the productName
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> matchedProducts = FXCollections.observableArrayList();

        productName = productName.toLowerCase();

        for (Product product : Inventory.getAllProducts()) {
            if (product.getName().toLowerCase().contains(productName)) {
                matchedProducts.add(product);
            }
        }
        return matchedProducts;
    }


    /**
     * @param index        the index of the updated part
     * @param selectedPart the part that will replace the previous part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * @param index      the index of the new product
     * @param newProduct the new product that will replace the previous product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart part to be deleted from allParts list
     * @return true if the part is deleted, false if not deleted
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * @param selectedProduct product to be deleted from allParts list
     * @return true if the product is deleted, false if not deleted
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return all parts in the list
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return all products in the list
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
