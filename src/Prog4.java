import java.math.BigInteger;

public class Prog4 {

    static BigInteger prime1 = null;
    static BigInteger prime2 = null;
    static String message = null;

    public static void main(String[] args) {
        
        args = new String[3];
        args[0] = "7";
        args[1] = "11";
        args[2] = "hashme";
        
        parseArgs(args);
        
        System.out.println("END OF LINE");
    }
    
    private static void parseArgs(String[] args) {
        
        //number of arguments
        if (args.length !=3) {
            System.err.println("Invalid number of arguments.");
            argsError();
        }
        
        //parse ints
        try {
            prime1 = new BigInteger(args[0]);
            prime2 = new BigInteger(args[1]);
            message = args[2];
        } catch (NumberFormatException e) {
            System.err.println("Could not parse numbers from arguments.");
            argsError();
        }
        
        //test for prime
        if (!prime1.isProbablePrime(1) || !prime2.isProbablePrime(1)) {
            System.err.println("One of the arguments was not prime.");
            argsError();
        }
        
        //test message length
        if (message.length() < 2 || message.length() > 6) {
            System.err.println("Message was an improper length");
            argsError();
        }
    }
    
    private static void argsError() {
        System.err.println("Please use 3 arguments [prime number] [prime number]"
                + " [2-6 character message]");
        System.exit(-1);
    }

}
