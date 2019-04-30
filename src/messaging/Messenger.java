package messaging;

import mining.Supplies;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Messenger implements Runnable {

    private static final boolean SHOW_ATTEMPTS = Boolean.getBoolean("messenger.show_attempts");

    private short id;
    private Docks dock;
    private List<Supplies> required;
    private Semaphore resources;
    private Supplies ready;

    public Messenger(short id, List<Supplies> required, Supplies ready, Docks dock) {
        this.id = id;
        this.dock = dock;
        this.required = required;
        this.resources = dock.messengers.get(ready);
        this.ready = ready;
    }

    public void run() {
        int count = 0;
        while(!Thread.interrupted()) {
            try {
                //Is my first resource available? Wait until it is.
                dock.isAvailable.get(required.get(count % required.size())).acquire();

                if(SHOW_ATTEMPTS)
                    System.out.printf("$$$ %s messenger has obtained resource %s.\n", ready,
                            required.get(count % required.size()));

                //Is my second resource available? Check and continue.
                if (dock.isAvailable.get(required.get((count + 1) % required.size())).tryAcquire()) {
                    //Take the resources
                    System.out.printf("%s messenger has obtained supplies %s and %s\n", ready, required.get(count % required.size()), required.get((count + 1) % required.size()));
                    //Let the miner know that their resources are delivered.
                    resources.release();
                    count = 0;
                } else { //If second resource is not available, let others use the first.
                    if(SHOW_ATTEMPTS)
                        System.out.println("Thread #" + id + " releasing materials back to docks");

                    dock.isAvailable.get(required.get(count % required.size())).release();
                    count = (count + 1);
                }
            } catch(InterruptedException ie) {
                return; // stop trying since someone told us to stop
            }
        }
    }
}
