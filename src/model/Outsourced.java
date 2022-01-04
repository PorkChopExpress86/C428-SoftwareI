package model;

/**
 * The class inherits from Part and is used to model the Outsourced parts that have a parameter companyName
 *
 * @author Blake Bowden
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * Constructor of the Outsourced part
     *
     * @param id          part id (int)
     * @param name        part name (String)
     * @param price       price of the part (double)
     * @param stock       number of parts in stock (int)
     * @param min         minimum number of parts in stock (int)
     * @param max         maximum number of parts in stock (int)
     * @param companyName name of the company that supplies the part (String)
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return name of the company that supplies the part (String)
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the name of the company to set (String)
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
