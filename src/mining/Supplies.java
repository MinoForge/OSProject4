package mining;

/**
 * An enum representing the supplies.
 *
 * @author Peter Gardner
 * @author Wesley Rogers
 * @version May 3, 2019
 */
public enum Supplies {
    BREAD("Bread"),
    BOLOGNA("Bologna"),
    CHEESE("Cheese");

    /**
     * An english representation of the enum.
     */
    private String type;

    Supplies(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
