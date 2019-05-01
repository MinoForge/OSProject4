package messaging;

import mining.Supplies;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The foreman of the simulation
 */
public class Foreman implements Runnable {

    /**
     * the thread id for the foreman
     */
    private short id;

    /**
     * the types of supplies that the foreman can give
     */
    private final Supplies[] types;

    /**
     * The docks to put things at.
     */
    private Docks dock;

    /**
     * Creates a foreman
     * @param id    the thread id to use
     * @param docks the docks we're using
     */
    public Foreman(Short id, Docks docks) {
        this.id = id;
        this.types = Supplies.values();
        dock = docks;
    }

    /**
     * Runs the simulation according to the required logic.
     */
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
                // if we interrupted, die
                return;
            }

            // Get supplies
            Random threadRng = ThreadLocalRandom.current();
            int type1Index = threadRng.nextInt(types.length);
            // start at index 1, then go a random amount around in a loop, without making a full
            // loop
            int type2Index = (type1Index + threadRng.nextInt(types.length - 1) + 1) % types.length;

            Supplies type1 = types[type1Index];
            Supplies type2 = types[type2Index];

            System.out.printf("The foreman is dropping %s and %s.\n", type1, type2);

            // Make resources available at the docks
            dock.isAvailable.get(type1).release();
            dock.isAvailable.get(type2).release();
        }


    }

    // TODO: Write any ather methods needed.
}

