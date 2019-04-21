import java.io.*;

public class Distribution {
    public static void main(String[] args){
        long timeout;
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
                }
            } else if(args[1].toUpperCase().charAt(0) != 'F') {
                usage(2);
            }
        } else {
            usage(3);
        }

        // TODO: 4/21/2019 Actually do things.
        System.exit(0);
    }

    private static void usage(int status) {
        System.out.println("USAGE: java Distribution <runtime> [Log to file? T/F]");
        System.exit(status);
    }

}
