import java.io.*;

public class Distribution {
    public static void main(String[] args){
        long timeout = 0;

        if(args.length > 0 && args.length < 3) {
            boolean badNumber = false;
            try {
                timeout = Long.parseLong(args[0]);
                if(timeout < 0) {
                    badNumber = true;
                }
            } catch(NumberFormatException nfe) {
                badNumber = true;
            }
            if(badNumber) {
                usage();
            }

            if(args.length > 1) {
                if(args[1].toUpperCase().charAt(0) == 'T') { //Matches any word starting with a t/T
                    try {
                        PrintStream out = new PrintStream(new File("log.txt"));
                        System.setOut(out);
                    } catch(FileNotFoundException fnfe) {
                        System.out.println("File could not be opened:  log.txt");
                    }
                }
            }
        } else {
            usage();
        }

        // TODO: 4/21/2019 Actually do things. 
    }

    public static void usage() {
        System.out.println("USAGE: java Distribution <runtime> [Log to file? T/F]");
    }

}
