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
        this.id = id;
        this.dock = dock;
        this.required = required;
        this.resources = dock.messengers.get(ready);
        this.ready = ready;
    }

    public void run() {
        int count = 0;
        while(!Thread.interrupted()) {
            if(count >= 3) {
                System.out.printf("##########%s messenger has flipped wait conditions %d.#########", ready, count);
            }
            try {
                //Is my first resource available? Wait until it is.
                dock.isAvailable.get(required.get(count % required.size())).acquire();

                //Is my second resource available? Check and continue.
                if (dock.isAvailable.get(required.get((count + 1) % required.size())).tryAcquire()) {
                //Take the resources
                    System.out.printf("%s messenger has obtained supplies %s and %s\n", ready, required.get(count % required.size()), required.get((count + 1) % required.size()));
//                    try {
                    //Let the miner know that their resources are delivered.
                    resources.release();
                    count = 0;
//                    } catch (InterruptedException ie) {
//                        // TODO: 4/20/2019 ?
//                    }
                } else { //If second resource is not available, let others use the first.
//                    System.out.println("Thread #" + id + " releasing materials back to docks");
//                    if(id == 2) {
//                        try {
//                            System.out.println("Bread mess going to sleep");
//                            System.out.flush();
//                            Thread.sleep(10000);
//                        } catch(InterruptedException ie) {
//
//                        }
//                    }
                    dock.isAvailable.get(required.get(count % required.size())).release();
                    count = (count + 1);
                }
            } catch(InterruptedException ie) {
                return;
            }

        }
    }
}
