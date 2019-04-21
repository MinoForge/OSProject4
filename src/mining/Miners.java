package mining;

public class Miners {

    private Supplies material;

    Miners(String type) {
        this.material = Supplies.valueOf(type);
    }


}
