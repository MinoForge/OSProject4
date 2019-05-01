import messaging.Docks;
import messaging.Foreman;
import messaging.Messenger;
import mining.Miner;
import mining.Supplies;
import util.SyncronizedPrintStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Entry point for the project
 */
public class Distribution {

    /**
     * The docks that this distribution is using.
     */
    private Docks docks;

    /**
     * The foreman overseeing this simulation.
     */
    private Foreman foreman;


    /**
     * Runs the simulation, stopping after the timeout occurs, if there is one.
     * @param timeout a length in seconds to timeout after, or a nonpositive integer to run forever
     */
    private void go(long timeout) {
        boolean runForever = true;
        if(timeout > 0) {
            runForever = false;
            System.out.println("Running for " + timeout + " seconds:");
        } else {
            System.out.println("Running forever:");
        }

        docks = new Docks();

        // signal the foreman to drop supplies initially
        docks.needSupplies.release();

        ArrayList<Thread> threads = this.createThreads();
        this.startThreads(threads);

        try {
            do{ // wait for timeout.
                Thread.sleep(timeout * 1000);
            } while(runForever);
        } catch (InterruptedException ie) {
            //Do nothing, since we're just gonna end the program anyways
        }

        for(Thread t : threads) {
            t.interrupt();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch(InterruptedException e) {
                // it went wrong while waiting for it to finish, it'll probably finish soon anyway
            }
        }
    }

    /**
     * Creates the threads for the miners, messengers, and the foreman.
     * @return an arraylist of all threads created.
     */
    private ArrayList<Thread> createThreads(){
        short threadID = 0;
        foreman = new Foreman(threadID++, docks);

        int numResources = Supplies.values().length;
        int totalThreads = numResources * 2 + 1;

        ArrayList<Thread> threads = new ArrayList<>(totalThreads);
        threads.add(new Thread(foreman));

        for(int i = 0; i < numResources; i++) {
            List<Supplies> resources = new ArrayList<>(Arrays.asList(Supplies.values()));
            Supplies ownedSupply = resources.remove(i);

            threads.add(new Thread(new Miner(threadID++, ownedSupply, docks)));
            threads.add(new Thread(new Messenger(threadID++, resources, ownedSupply, docks)));
        }

        return threads;
    }

    /**
     * Starts the threads provided in a random order.
     * @param threads the threads to start
     */
    private void startThreads(List<Thread> threads){
        int threadsStarted = 0;
        while(threadsStarted < threads.size()) { //Loop until all threads started.
            int rand = ThreadLocalRandom.current().nextInt(threads.size()); //Random thread index
            if(threads.get(rand).getState().equals(Thread.State.NEW)) { //If thread not alive:
                System.out.println("Starting process " + rand);
                threads.get(rand).start(); //Start it
                threadsStarted++;
            }
        }
    }



    /**
     * Entry point for the program
     * @param args args[0] is the number of seconds the program should run, args[1] is the t/f on
     *            whether the program should log to log.txt
     */
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

        // Using an AOP framework is the correct way to do this,
        // but I'm not about to add that dependency.
        System.setOut(new SyncronizedPrintStream(System.out));

        new Distribution().go(timeout);
        System.exit(0);
    }

    /**
     * Prints a usage message and exits
     * @param status status code to exit with
     */
    private static void usage(int status) {
        System.out.println("USAGE: java Distribution <runtime> [Log to file? T/F]");
        System.exit(status);
    }

}
