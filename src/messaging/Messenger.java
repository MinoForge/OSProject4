package messaging;

import mining.Supplies;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * A class representing the messengers delivering supplies.
 */
public class Messenger implements Runnable {

    /**
     * Determines whether to show attempts to acquire all resources.
     */
    private static final boolean SHOW_ATTEMPTS = Boolean.getBoolean("messenger.show_attempts");

    /**
     * The thread's id
     */
    private short id;

    /**
     * The dock the messenger is interacting with
     */
    private Docks dock;

    /**
     * The supplies that this messenger needs.
     */
    private List<Supplies> required;

    /**
     * A semaphore representing if this messenger has supplies for sandwiches available.
     */
    private Semaphore resources;

    /**
     * What resource that this messengers mine has available.
     */
    private Supplies ready;

    /**
     * Creates a messenger.
     * @param id        the id
     * @param required  list of required supplies
     * @param ready     the available supply
     * @param dock      the dock
     */
    public Messenger(short id, List<Supplies> required, Supplies ready, Docks dock) {
        this.id = id;
        this.dock = dock;
        this.required = required;
        this.resources = dock.messengers.get(ready);
        this.ready = ready;
    }

    /**
     * Runs the logic of messengers.
     */
    public void run() {
        int count = 0;
        // the switching algorithm here is all Peter's beautiful brain
        // the idea behind this algorithm is that we try to switch the waiting on semaphores so that
        // the two that are not able to acquire resources are waiting on the resource semaphore that
        // has not been released, while the one that is able to take switches if it isn't able to
        // take things immediately. The end result is that the two unable to progress are no
        // longer waiting on the semaphores available, and the correct thread can take both.
        while(!Thread.interrupted()) {
            try {
                //Is my first resource available? Wait until it is.
                dock.isAvailable.get(required.get(count % required.size())).acquire();

                if(SHOW_ATTEMPTS)
                    System.out.printf("%s messenger has obtained resource %s.\n", ready,
                            required.get(count % required.size()));

                //Is my second resource available? Check and continue.
                if (dock.isAvailable.get(required.get((count + 1) % required.size())).tryAcquire()) {
                    //Take the resources
                    System.out.printf("%s messenger has obtained resource %s and %s.\n", ready,
                            required.get(count % required.size()), required.get((count + 1) % required.size()));
                    //Let the miner know that their resources are delivered.
                    resources.release();
                    count = 0;
                } else { //If second resource is not available, let others use the first.
                    if(SHOW_ATTEMPTS)
                        System.out.printf("%s messenger releasing materials back to docks.\n",
                                ready);

                    dock.isAvailable.get(required.get(count % required.size())).release();
                    count = (count + 1);
                }
            } catch(InterruptedException ie) {
                return; // stop trying since someone told us to stop
            }
        }
    }
}
