package mining;

import messaging.Messenger;

import java.util.concurrent.ThreadLocalRandom;

public class Miner implements Runnable {

    private Supplies material;
    private Messenger messenger;

    Miner(String type, Messenger messenger) {
        this.material = Supplies.valueOf(type);
        this.messenger = messenger;

    }

    public void run() {
        while(!Thread.interrupted()){
            makeSandwiches();
            eatSandwiches();
        }
    }

    public void makeSandwiches(){
        System.out.printf("The %s miners want to eat.\n", material);

        try {
            // sleep for 10s max
            messenger.getResources().release();
            Thread.sleep(ThreadLocalRandom.current().nextInt(10000));
        } catch (InterruptedException e){
            // someone said stop, lets let the miners eat first.
            Thread.currentThread().interrupt();
        }
    }

    public void eatSandwiches() {
        System.out.printf("The %s miners are eating. Om nom nom.\n", material);

        try {
            // sleep for 10s max
            Thread.sleep(ThreadLocalRandom.current().nextInt(10000));
        } catch (InterruptedException e){
            //someone said stop, I say no
        }
    }


}
