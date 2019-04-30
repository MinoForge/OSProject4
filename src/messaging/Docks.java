package messaging;

import mining.Supplies;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Docks {

    public Map<Supplies, Semaphore> isAvailable;

    public Map<Supplies, Semaphore> messengers;

    public Semaphore needSupplies;

    public Docks() {
        isAvailable = Collections.synchronizedMap(new HashMap<>());
        for(Supplies supply : Supplies.values())
            isAvailable.put(supply, new Semaphore(0, true));

        this.messengers = Collections.synchronizedMap(new HashMap<>());
        for(Supplies supply : Supplies.values())
            messengers.put(supply, new Semaphore(1, true));

        this.needSupplies = new Semaphore(1, true);

    }

//    public Map<Supplies, Semaphore> getIsAvailable() {
//        return isAvailable;
//    }
}
