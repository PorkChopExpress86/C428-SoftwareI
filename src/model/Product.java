package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class Product will model a product object. A product consists of an array of parts and has multiple attributes that
 * describe the product.
 *
 * @author Blake Bowden
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor of a product
     *
     * @param id    the id of the product name(integer)
     * @param name  the product name (string)
     * @param price the amount the product costs (double)
     * @param stock the number of units available
     * @param min   the minimum number of units that should be in stock
     * @param max   the maximum number of units that should be in stock
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param part the part object makes some of the product
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the associated part of the product
     * @return true if the part was remove, false if the part was not removed
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return a list of the part objects that make up the Product object
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * @return the id of the product
     */
    // Getters and setters
    public int getId() {
        return id;
    }

    /**
     * @param id integer of the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @param name product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price price to be set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the number of units in stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock number of units in stock to be set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the minimum number of products allowed in stock
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min minimum number of products allowed in stock to be set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return maximum number of products allowed in stock
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max maximum number of products allowed in stock to be set
     */
    public void setMax(int max) {
        this.max = max;
    }
}
