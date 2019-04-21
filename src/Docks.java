import mining.Supplies;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Docks {

    private Map<Supplies, Semaphore> isAvailable;

    private List<Supplies> resources;

    public Docks() {
        isAvailable = Collections.synchronizedMap(new HashMap<>());

        for(Supplies supply : Supplies.values())
            isAvailable.put(supply, new Semaphore(1, true));

        this.resources = Collections.synchronizedList(new ArrayList<>(2));
    }

    public synchronized void deliver(Supplies type){
        resources.add(type);

        try {
            isAvailable.get(type).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
