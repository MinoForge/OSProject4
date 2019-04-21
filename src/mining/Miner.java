package mining;

public class Miner implements Runnable {

    private Supplies material;

    Miner(String type) {
        this.material = Supplies.valueOf(type);
    }

    public void run() {

    }

    public void makeSammiches(){

    }


}
