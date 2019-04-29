package messaging;

import mining.Supplies;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Messenger implements Runnable {

    private Docks dock;
    private List<Supplies> required;
    private Semaphore resources;

    public Messenger(List<Supplies> required, Supplies ready, Docks dock) {
        this.dock = dock;
        this.required = required;
        this.resources = dock.messengers.get(ready);
    }

    public void run() {
        int count = 0;
        while(!Thread.interrupted()) {

            try {
                //Is my first resource available? Wait until it is.
                dock.getIsAvailable().get(required.get(count)).acquire();

                //Is my second resource available? Check and continue.
                if (dock.getIsAvailable().get(required.get((count + 1) % required.size())).tryAcquire()) {

                    //Take the resources
                    try {
                        //Let the miner know that their resources are delivered.
                        resources.acquire();
                    } catch (InterruptedException ie) {
                        // TODO: 4/20/2019 ?
                    }
                } else { //If second resource is not available, let others use the first.

                    dock.getIsAvailable().get(required.get(count)).release();
                    count = (count + 1) % required.size();
                }
            } catch(InterruptedException ie) {
                // TODO: 4/21/2019
            }

        }
    }
}
