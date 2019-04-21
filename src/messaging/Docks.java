package messaging;

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
        resources.add(type); //Add the given supply to the list of currently available supplies

        isAvailable.get(type).release(); //Allow others to see that the resource is available
    }

    public List<Supplies> getResources() {
        return resources;
    }

    public Map<Supplies, Semaphore> getIsAvailable() {
        return isAvailable;
    }
}
