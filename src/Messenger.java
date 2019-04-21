import mining.Miner;
import mining.Supplies;

public class Messenger {

    private Docks dock;
    private Miner miner;
    private Supplies[] observe;

    public Messenger(Docks dock, Miner miner, Supplies[] observe) {
        this.dock = dock;
        this.miner = miner;
        this.observe = observe;
    }



}
