package model;

/**
 * This class inherits from Part is for the InHouse parts that have the attribute machineId
 *
 * @author Blake Bowden
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * Constructor of the in house part
     *
     * @param id        part id (int)
     * @param name      part name (String)
     * @param price     price of the part (double)
     * @param stock     number of parts in stock (int)
     * @param min       minimum number of parts in stock (int)
     * @param max       maximum number of parts in stock (int)
     * @param machineId id of the machine that makes the parts (int)
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the id of the machine that ameks the parts
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId set the id of the machine that makes the part
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
