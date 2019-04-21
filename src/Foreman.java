// import??  

import mining.Supplies;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Foreman implements Runnable {

    private final Supplies[] types;

    private final Docks theDock;

    private volatile boolean running;

    public Foreman(Docks docks) {
        this.types = Supplies.values();
        theDock = docks;
    }

    @Override
    public void run() {
        /* TODO: 
         * Pick two supplies randomly, the two supplies will always be different from each other.
         * Signal the 'docks' indicating supplies are ready for EACH type of supply chosen.
         * Foreman will NOT send any more supplies of any kind until the two picked have been
         * consumed.
         */
        while(running) {

            Random threadRng = ThreadLocalRandom.current();
            int type1Index = threadRng.nextInt(types.length);
            // start at index 1, then go a random amount around in a loop, without making a full
            // loop
            int type2Index = (type1Index + threadRng.nextInt(types.length - 2) + 1) % types.length;
            Supplies type1 = types[type1Index];
            Supplies type2 = types[type2Index];

            synchronized (theDock) {
                // deliver to the dock
                theDock.deliver(type1);
                theDock.deliver(type2);
                // wake the dock up and say I gave it something
                theDock.notify();
            }

            synchronized (theDock) {
                try {
                    // wait for the dock to be empty
                    theDock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Foreman was interrupted, goodbye.");

                }
            }
        }


    }

    // TODO: Write any ather methods needed.
}

