package messaging;

import mining.Supplies;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Messenger implements Runnable {

    private short id;
    private Docks dock;
    private List<Supplies> required;
    private Semaphore resources;
    private Supplies ready;

    public Messenger(short id, List<Supplies> required, Supplies ready, Docks dock) {
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
                dock.isAvailable.get(required.get(count)).acquire();

                //Is my second resource available? Check and continue.
                    if (dock.isAvailable.get(required.get((count + 1) % required.size())).tryAcquire()) {
                    //Take the resources
                    System.out.printf("%s messenger has obtained supplies: %s | %s\n", ready, required.get(count), required.get((count + 1) % required.size()));
//                    try {
                        //Let the miner know that their resources are delivered.
                        resources.release();
//                    } catch (InterruptedException ie) {
//                        // TODO: 4/20/2019 ?
//                    }
                } else { //If second resource is not available, let others use the first.

                    dock.isAvailable.get(required.get(count)).release();
                    count = (count + 1) % required.size();
                }
            } catch(InterruptedException ie) {
                // TODO: 4/21/2019
            }

        }
    }
}
