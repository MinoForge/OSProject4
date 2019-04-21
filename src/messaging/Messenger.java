package messaging;

import mining.Miner;
import mining.Supplies;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Messenger implements Runnable {

    private Docks dock;
    private Miner miner;
    private List<Supplies> required;
    private Semaphore resources;
    private volatile boolean running;

    public Messenger(Docks dock, Miner miner, List<Supplies> required) {
        this.dock = dock;
        this.miner = miner;
        this.required = required;
    }

    public void run() {
        while(running) {
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
