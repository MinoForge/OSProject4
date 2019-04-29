package mining;

import messaging.Docks;

import java.util.concurrent.ThreadLocalRandom;

public class Miner implements Runnable {

    private Docks dock;

    private Supplies material;

    public Miner(Supplies material, Docks docks) {
        this.material = material;
        this.dock = docks;

    }

    public void run() {
        while(!Thread.interrupted()){
            makeSandwiches();
            eatSandwiches();
        }
    }

    private void makeSandwiches(){
        System.out.printf("The %s miners want to eat.\n", material);

        try {
            // sleep for 10s max
            dock.needSupplies.release();
            dock.messengers.get(material).acquire();
//            Thread.sleep(ThreadLocalRandom.current().nextInt(10000));
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
        } catch (InterruptedException e){
            // someone said stop, lets let the miners eat first.
            Thread.currentThread().interrupt();
        }
    }

    private void eatSandwiches() {
        System.out.printf("The %s miners are eating. Om nom nom.\n", material);

        try {
            // sleep for 10s max
            Thread.sleep(ThreadLocalRandom.current().nextInt(10000));
        } catch (InterruptedException e){
            //someone said stop, I say no
        }
    }


}
