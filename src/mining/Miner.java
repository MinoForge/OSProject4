package mining;

import messaging.Docks;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A class representing the food miners.
 */
public class Miner implements Runnable {

    /**
     * The upper bound on how long a miner can wait while making or eating sandwiches. Default
     * 10,000 ms.
     */
    private static final int BOUND = Integer.getInteger("miner.bound", 10000);

    /**
     * Whether or not to show the status of the miners. Default true.
     */
    private static final boolean SHOW_STATUS = !Boolean.getBoolean("miner.hide_status");

    /**
     * The miner id.
     */
    private short id;

    /**
     * The docks we're using.
     */
    private Docks dock;

    /**
     * The supplies we produce.
     */
    private Supplies material;

    public Miner(short id, Supplies material, Docks docks) {
        this.id = id;
        this.material = material;
        this.dock = docks;
    }

    /**
     * Runs the logic of the miners, which is making and eating sandwiches forever.
     */
    public void run() {
        try {
            while (!Thread.interrupted()) {
                makeSandwiches();
                eatSandwiches();
            }
        } catch (InterruptedException e){
            // just fall off, since we shouldn't return from a catch
        }
    }

    /**
     * Makes sandwiches
     * @throws InterruptedException if we're interrupted while waiting on resources or sleeping
     */
    private void makeSandwiches() throws InterruptedException {
        if(SHOW_STATUS)
            System.out.printf("The %s miners want to eat.\n", material);

        dock.messengers.get(material).acquire(); // get the food from the messenger
        dock.needSupplies.release(); // say that we got it and the docks need more food
        Thread.sleep(ThreadLocalRandom.current().nextInt(BOUND)); // make sammich
    }

    /**
     * Eats sandwiches
     * @throws InterruptedException if we get interrupted while eating. Rude.
     */
    private void eatSandwiches() throws InterruptedException {
        if(SHOW_STATUS)
            System.out.printf("The %s miners are eating. Om nom nom.\n", material);
        Thread.sleep(ThreadLocalRandom.current().nextInt(BOUND)); // eat sammich
    }


}
