package mining;

public enum Supplies {
    BREAD("Bread"),
    BOLOGNA("Bologna"),
    CHEESE("Cheese");

    private String type;

    Supplies(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
