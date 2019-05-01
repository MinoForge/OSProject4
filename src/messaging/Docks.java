package messaging;

import mining.Supplies;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * The docks, which is the shared memory of the program.
 *
 * Aside from the state of the semaphores, the object is immutable.
 */
public class Docks {

    /**
     * A map of whether a given supply is available.
     */
    public final Map<Supplies, Semaphore> isAvailable;

    /**
     * A map to whether the messenger for a given supply is ready.
     */
    public final Map<Supplies, Semaphore> messengers;

    /**
     * A signal to request more supplies.
     */
    public final Semaphore needSupplies;

    /**
     * Creates the maps and semaphores required, and makes the object immutable.
     */
    public Docks() {
        Map<Supplies, Semaphore> isAvailable = Collections.synchronizedMap(new HashMap<>());

        for(Supplies supply : Supplies.values())
            isAvailable.put(supply, new Semaphore(0, true));

        this.isAvailable = Collections.unmodifiableMap(isAvailable);

        Map<Supplies, Semaphore> messengers = Collections.synchronizedMap(new HashMap<>());
        for(Supplies supply : Supplies.values())
            messengers.put(supply, new Semaphore(0, true));

        this.messengers = Collections.unmodifiableMap(messengers);

        this.needSupplies = new Semaphore(0, true);

    }
}
