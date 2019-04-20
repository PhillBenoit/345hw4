import java.math.BigInteger;

/**
 * Modular Arithmetic Secure Hash
 * 
 * @author Phillip Benoit
 *
 */
public class Prog4 {
    
    /**
     * Holds the message
     */
    static private String message = null;
    
    /**
     * BigInt value of the product of the two primes
     */
    static private BigInteger M;

    /**
     * Entry point
     * 
     * @param args command line arguments
     * [prime number] [prime number] [message]
     */
    public static void main(String[] args) {
        
        /*
        //for eclipse testing
        args = new String[3];
        args[0] = "52026427209861890870348378323378284729698009109265013619678"
                + "72059486045713145450116712488685004691423";
        args[1] = "72126101472954749095445237850434924099693821481867654600825"
                + "00085393519556525921455588705423020751421";
        args[2] = "-crypt";
        */
        
        //step 1
        //done when parsing arguments
        
        //step 2
        int n = parseArgs(args);
        
        //step 3
        BigInteger H = new BigInteger("0");
        BigInteger A = new BigInteger(padRight("1111", n),2);

        //step 4
        int b = message.length();
        int n2 = n/2;
        int t = message.length()/n2;
        if (message.length()%n2!=0) t++;
        message = padRight(message, t*n2);
        
        //step 5
        String[] x = new String[t+1];
        for (int step = 0; step < t; step++)
            x[step] = message.substring(step*n2, (step+1)*n2);
        
        //step 6
        x[t] = padLeft(Integer.toBinaryString(b), n2);
        
        //step 7
        String[] y = new String[t+1];
        int nybbles = n2/4;
        for (int step = 0; step < t; step++) {
            y[step] = "";
            for (int nyb = 0; nyb < nybbles; nyb++)
                y[step] += "1111" + x[step].substring(nyb*4, (nyb+1)*4);
        }
        
        //step 8
        y[t] = "";
        for (int nyb = 0; nyb < nybbles; nyb++)
            y[t] += "1010" + x[t].substring(nyb*4, (nyb+1)*4);
        
        //step 9
        
        //used for mod to create G
        BigInteger nBits = BigInteger.valueOf(2).pow(n);
        
        for (String yStep:y) {
            
            //holds BigInt value for current y string
            BigInteger Y = new BigInteger(yStep, 2);
            
            BigInteger F = H.xor(Y);
            F = F.or(A);
            F = F.pow(257);
            F = F.mod(M);
            
            BigInteger G = F.mod(nBits);
            
            H = G.xor(H);
        }
        
        //final output
        System.out.format("Binary: %s\n"
                + "Decimal: %s\n", H.toString(2), H.toString());
    }
    
    /**
     * Parse command line arguments to test for validity.
     * also performs step 1
     * 
     * @param args command line arguments
     * @return value of n
     */
    private static int parseArgs(String[] args) {
        
        //number of arguments
        if (args.length !=3) {
            System.err.println("Invalid number of arguments.");
            argsError();
        }
        
        BigInteger prime1 = null, prime2 = null;
        
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
        if (!prime1.isProbablePrime(10) || !prime2.isProbablePrime(10)) {
            System.err.println("One of the arguments was not prime.");
            argsError();
        }
        
        //test message length
        if (message.length() < 2 || message.length() > 6) {
            System.err.println("Message was an improper length");
            argsError();
        }
        
        //test prime product size
        M = prime1.multiply(prime2);
        int m = M.bitLength();
        if (m < 16) {
            System.err.format("Product of the prime numbers was too small. "
                    + "(must be at least 32768 was %s)\n", M.toString());
            argsError();
        }
        
        //parse message into binary
        String big_int_seed = "";
        for (char letter: message.toCharArray())
            big_int_seed += padLeft(Integer.toBinaryString(letter), 7);
        message = big_int_seed;
        
        /* old code that removes leading 0s from first char
        String big_int_seed = Integer.toBinaryString(message.charAt(0));
        for (int step = 1; step < message.length(); step++)
            big_int_seed += padLeft(Integer.toBinaryString(
                    message.charAt(step)), 7);
        message = big_int_seed;
        */
        
        //return n
        int n = m/16;
        return n*16;
    }
    
    /**
     * Pads a binary string to the right
     * (increasing value)
     * 
     * @param number string to pad
     * @param padding length of new string
     * @return new string
     */
    private static String padRight(String number, int padding) {
        return String.format("%-"+padding+"s", number).replace(" ", "0");
    }
    
    /**
     * Pads a binary string to the left
     * (leading 0s)
     * 
     * @param number string to pad
     * @param padding length of new string
     * @return new string
     */
    private static String padLeft(String number, int padding) {
        return String.format("%"+padding+"s", number).replace(" ", "0");
    }
    
    /**
     * Print command line instructions and exit program
     */
    private static void argsError() {
        System.err.println("Please use 3 arguments [prime number] [prime number]"
                + " [2-6 character message]");
        System.exit(1);
    }

}
