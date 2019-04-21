package messaging;

import mining.Supplies;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Messenger implements Runnable {

    private Docks dock;
    private List<Supplies> required;
    private Semaphore resources;

    public Messenger(Docks dock, List<Supplies> required) {
        this.dock = dock;
        this.required = required;
        this.resources = new Semaphore(1);
    }

    public void run() {
        while(!Thread.interrupted()) {

            try {
                //Is my first resource available? Wait until it is.
                dock.getIsAvailable().get(required.get(0)).acquire();

                //Is my second resource available? Check and continue.
                if (dock.getIsAvailable().get(required.get(1)).tryAcquire()) {

                    //Take the resources
                    dock.getResources().clear();
                    try {
                        //Let the miner know that their resources are delivered.
                        resources.acquire();
                    } catch (InterruptedException ie) {
                        // TODO: 4/20/2019 ?
                    }
                } else { //If second resource is not available, let others use the first.
                    dock.getIsAvailable().get(required.get(0)).release();
                }
            } catch(InterruptedException ie) {
                // TODO: 4/21/2019
            }

        }
    }

    public Semaphore getResources() {
        return resources;
    }
}
