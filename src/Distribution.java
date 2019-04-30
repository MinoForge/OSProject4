import messaging.Docks;
import messaging.Foreman;
import messaging.Messenger;
import mining.Miner;
import mining.Supplies;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Distribution {
    public static void main(String[] args){
        long timeout = 0;
        if(args.length > 0 && args.length < 3) {
            boolean badNumber = false;
            try {
                timeout = Long.parseLong(args[0]);
            } catch(NumberFormatException nfe) {
                badNumber = true;
            }
            if(badNumber) {
                usage(1);
            }

            if(args.length > 1) {
                if(args[1].toUpperCase().charAt(0) == 'T') { //Matches any word starting with a t/T
                    try {
                        PrintStream out = new PrintStream(new File("log.txt"));
                        System.setOut(out);
                    } catch(FileNotFoundException fnfe) {
                        System.out.println("File could not be opened:  log.txt");
                    }
                } else if(args[1].toUpperCase().charAt(0) != 'F') {
                    usage(2);
                }
            }
        } else {
            usage(3);
        }


        go(timeout);
        // TODO: 4/21/2019 Actually do things.
        System.exit(0);
    }

    private static void go(long timeout) {
        boolean runForever = true;
        if(timeout > 0) {
            runForever = false;
            System.out.println("Running for " + timeout + " seconds:");
        } else {
            System.out.println("Running forever:");
        }
        short threadID = 0;

        Docks docks = new Docks();

        Foreman foreman = new Foreman(threadID++, docks);

        int numResources = Supplies.values().length;
        int totalThreads = numResources * 2 + 1;

        ArrayList<Thread> threads = new ArrayList<>(totalThreads);
        threads.add(new Thread(foreman));

        for(int i = 0; i < numResources; i++) {
            List<Supplies> resources = new ArrayList<>(Arrays.asList(Supplies.values()));
            Supplies ownedSupply = resources.get(i);

            resources.remove(i);

            threads.add(new Thread(new Miner(threadID++, ownedSupply, docks)));
            threads.add(new Thread(new Messenger(threadID++, resources, ownedSupply, docks)));
        }

        //Start in a random order, because testing
        int threadsStarted = 0;
        while(threadsStarted < totalThreads) { //Loop until all threads started.
            int rand = ThreadLocalRandom.current().nextInt(7); //Random thread index
            if(threads.get(rand).getState().equals(Thread.State.NEW)) { //If thread not alive:
                System.out.println("Starting process " + rand);
                threads.get(rand).start(); //Start it
                threadsStarted++;
            }
        }

        boolean notFinished = true;
        try {
            while(runForever || notFinished) {
//                Thread.sleep(timeout * 1000);
                Thread.sleep(500);

                notFinished = false;
            }
        } catch (InterruptedException ie) {
            //Do nothing, since we're just gonna end the program anyways
        }

        // TODO: 4/29/2019 THREAD CLEANUP?
        for(int i = 0; i < totalThreads; i++) {
            threads.get(i).interrupt();
        }



    }

    private static void usage(int status) {
        System.out.println("USAGE: java Distribution <runtime> [Log to file? T/F]");
        System.exit(status);
    }

}
