package mining;

import java.util.concurrent.ThreadLocalRandom;

public class Miner implements Runnable {

    private Supplies material;

    Miner(String type) {
        this.material = Supplies.valueOf(type);
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
