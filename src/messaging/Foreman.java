package messaging;

import mining.Supplies;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Foreman implements Runnable {

    private final Supplies[] types;

    private Docks dock;

    private volatile boolean running;

    public Foreman(Docks docks) {
        this.types = Supplies.values();
        dock = docks;
    }

    @Override
    public void run() {
        /* TODO: 
         * Pick two supplies randomly, the two supplies will always be different from each other.
         * Signal the 'docks' indicating supplies are ready for EACH type of supply chosen.
         * messaging.Foreman will NOT send any more supplies of any kind until the two picked have been
         * consumed.
         */
        while(!Thread.interrupted()) {
            try {
                dock.needSupplies.acquire();
            } catch (InterruptedException ie) {

            }

            // Get supplies
            Random threadRng = ThreadLocalRandom.current();
            int type1Index = threadRng.nextInt(types.length);
            // start at index 1, then go a random amount around in a loop, without making a full
            // loop
            int type2Index = (type1Index + threadRng.nextInt(types.length - 2) + 1) % types.length;

            System.out.printf("Dropping %s\n", types[type1Index]);
            Supplies type1 = types[type1Index];

            System.out.printf("Dropping %s\n", types[type2Index]);
            Supplies type2 = types[type2Index];

            // Make resources available at the docks
            dock.isAvailable.get(type1).release();
            dock.isAvailable.get(type2).release();
        }


    }

    // TODO: Write any ather methods needed.
}

