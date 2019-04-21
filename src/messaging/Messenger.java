package messaging;

import mining.Miner;
import mining.Supplies;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Messenger implements Runnable {

    private Docks dock;
    private List<Supplies> required;
    private Semaphore resources;

    public Messenger(Docks dock, Miner miner, List<Supplies> required) {
        this.dock = dock;
        this.required = required;
        this.resources = new Semaphore(1);
    }

    public void run() {
        while(!Thread.interrupted()) {
            synchronized (dock) {
                if (dock.getResources().containsAll(required)) {
                    dock.getResources().clear();
                    try {
                        resources.acquire();
                    } catch(InterruptedException ie) {
                        // TODO: 4/20/2019 ?
                    }
                }
            }
        }
    }

    public Semaphore getResources() {
        return resources;
    }
}
