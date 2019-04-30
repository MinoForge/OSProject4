package messaging;

import mining.Supplies;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Foreman implements Runnable {

    private short id;

    private final Supplies[] types;

    private Docks dock;

    public Foreman(Short id, Docks docks) {
        this.id = id;
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

            System.out.printf("1Dropping %s\n", types[type1Index]);
            Supplies type1 = types[type1Index];

            System.out.printf("2Dropping %s\n", types[type2Index]);
            Supplies type2 = types[type2Index];
            System.out.printf("Bread available at docks:   " + dock.isAvailable.get(Supplies.BREAD).toString().split("\\[|]")[1] + "\n");
            System.out.printf("Bologna available at docks: " + dock.isAvailable.get(Supplies.BOLOGNA).toString().split("\\[|]")[1] + "\n");
            System.out.printf("Cheese available at docks:  " + dock.isAvailable.get(Supplies.CHEESE).toString().split("\\[|]")[1] + "\n");
            System.out.printf("Resources to Bread Miners:    " + dock.messengers.get(Supplies.BREAD).toString().split("\\[|]")[1] + "\n");
            System.out.printf("Resources to Bologna Miners:  " + dock.messengers.get(Supplies.BOLOGNA).toString().split("\\[|]")[1] + "\n");
            System.out.printf("Resources to Cheese Miners:   " + dock.messengers.get(Supplies.CHEESE).toString().split("\\[|]")[1] + "\n");
            System.out.printf("State of Need Resources:   " + dock.needSupplies.toString().split("\\[|]")[1] + "\n");

            try {
                Thread.sleep(100);
            }catch(InterruptedException ie) {
                //
            }

            // Make resources available at the docks
//            try {
                dock.isAvailable.get(type1).release();
                dock.isAvailable.get(type2).release();
//            } catch (InterruptedException ie) {
//
//            }
        }


    }

    // TODO: Write any ather methods needed.
}

