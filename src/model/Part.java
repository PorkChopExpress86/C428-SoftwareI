package model;

/**
 * @author Blake Bowden
 */
public class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * @param id id of the part (int)
     * @param name name of the part (String)
     * @param price price of the part (double)
     * @param stock number of parts in stock (int)
     * @param min minimum number of parts in stock (int)
     * @param max maximum number of parts in stock (int)
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the name of the part
     */
    public int getId() {
        return id;
    }

    /**
     * @param id set the id of the part
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name of the part
     */
    public String getName() {
        return name;
    }

    /**
     * @param name set the name of the part
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price of one part
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price set the price per part
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the number of parts available in stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock set the number of parts available in stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the minimum number of parts allowed in stock
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min set the minimum number of parts allowed in stock
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the maximum number of parts allowed in stock
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max set the maximum number of parts allowed in stock
     */
    public void setMax(int max) {
        this.max = max;
    }
}
