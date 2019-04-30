package mining;

import messaging.Docks;

import java.util.concurrent.ThreadLocalRandom;

public class Miner implements Runnable {

    private static final int BOUND = Integer.getInteger("miner.bound", 10);

    private static final boolean SHOW_STATUS = !Boolean.getBoolean("miner.hide_status");

    private short id;

    private Docks dock;

    private Supplies material;

    public Miner(short id, Supplies material, Docks docks) {
        this.material = material;
        this.dock = docks;

    }

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

    private void makeSandwiches() throws InterruptedException {
        if(SHOW_STATUS)
            System.out.printf("The %s miners want to eat.\n", material);

        dock.needSupplies.release();
        dock.messengers.get(material).acquire();
        Thread.sleep(ThreadLocalRandom.current().nextInt(BOUND));
    }

    private void eatSandwiches() throws InterruptedException {
        if(SHOW_STATUS)
            System.out.printf("The %s miners are eating. Om nom nom.\n", material);
        Thread.sleep(ThreadLocalRandom.current().nextInt(BOUND));
    }


}
