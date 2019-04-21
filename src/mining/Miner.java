package mining;

public class Miner {

    private Supplies material;

    Miner(String type) {
        this.material = Supplies.valueOf(type);
    }


}
