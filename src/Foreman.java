// import??  

import mining.Supplies;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Foreman implements Runnable {

    private final Supplies[] types;

    private final Docks theDock;

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
        Random threadRng = ThreadLocalRandom.current();
        int type1Index = threadRng.nextInt(types.length);
        int type2Index = (type1Index + threadRng.nextInt(types.length - 1)) % types.length;
        Supplies type1 = types[type1Index];
        Supplies type2 = types[type2Index];

        synchronized (theDock) {
            theDock.notify();
        }



    }

    // TODO: Write any ather methods needed.
}

