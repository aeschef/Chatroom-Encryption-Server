import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private int[] privateKey;
    private int cesarShift;

    //constructor
    public ClientHandler (Socket socket) {
        this.clientSocket = socket;
    }

    /**
     * business logic?
     */
    public void run(){
        System.out.println("<CH>: starting run...");
        //reader and writer
        PrintWriter out = null;
        BufferedReader in = null;
        try{

            //get the outputstream of client
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            //get the inputstream of client
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("<CH>:\n Entering call& response phase.");

            out.println("Enter 'y' if you would like a public key");

            String wouldLikeKey = in.readLine();
            if(wouldLikeKey.equals("y")){
                System.out.println("The client wants a key");
                int[] pk = generatePublicKey();
                out.println(Arrays.toString(pk));
                String cs = decryptFromPublic(in.readLine());
                System.out.println(cs);//WHY
                cesarShift = Integer.parseInt(cs);
                System.out.println("<Server> \n CesarShift: " + cesarShift);
                String str = encryptCesar("TOPSECRETGOVERNMENTINTELLIGENCE");
                //System.out.println(str);
                out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {

                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    
    }

    /**
     * encrypts Using established cesar Cipher
     * @return String containing encrypted Message
     */
    public String encryptCesar(String str){
        String result = "";
        char[] text = str.toCharArray();
        // traverse text
        for (int i=0;i<text.length;i++)
        {
            
            // apply transformation to each character
            // Encrypt Uppercase letters
            if (isupper(text[i])){
                result += (char)(((int)(text[i])+cesarShift -65)%26 +65);
            }
            else{
                result += (char)(((int)(text[i])+cesarShift-97)%26+97);
            }
        }
        // Return the resulting string
        return result;
    }

    private boolean isupper(char c){
        return (int)c >= 65 && (int)c < 91;
    }
    /**
     * generates public key and cues creation of private key
     * 
     * @return int[] of public key [coprime, modulus]
     */
    private int[] generatePublicKey() {
        // Numbers the form base for public key..... change to randomly generation later
        BigInteger rp1 = new BigInteger(randPrime());
        BigInteger rp2 = new BigInteger(randPrime());

        // used in calculation of encryption and in private key.
        BigInteger modulus = rp1.multiply(rp2);

        // used in creation of coPrimes to create public and private keys
        BigInteger totient = (rp1.subtract(new BigInteger("1"))).multiply(rp2.subtract(new BigInteger("1")));

        int coPrime = getCoPrime(totient);

        // array to store [coPrime, modulus]
        int[] publicKey = new int[2];
        publicKey[0] = coPrime;
        publicKey[1] = modulus.intValue();
        // creates private key compatible with public key just created.
        generatePrivateKey(publicKey, totient);

        return publicKey;
    }

    /**
     * 
     * @return randomly generated prime number in String 
     */
    private String randPrime(){
        int num = 0; 
        Random rand = new Random();
            for(int o=0;o<10;o++){
            num = rand.nextInt(1000) + 1; 
            while (!isPrime(num)) { 
                num = rand.nextInt(1000) + 1;
            }  
        }
        return num + "";      
    } 
     
    /**
     * 
     * @param inputNum int to check for prime-ness
     * @return whether inputeNum is prime
     */
    private static boolean isPrime(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0)         
            return inputNum == 2 || inputNum == 3;

            int divisor = 3; 
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0)) 
            divisor += 2;
            
        return inputNum % divisor != 0; 
            
    } 
             
    

    /**
     * 
     * @param num Big Integer that is totient to get coPrime of
     * @return a coprime of num chosen at random from available choices
     */
    private int getCoPrime(BigInteger num) {
        int[] coPrimes = getCoprimeArray(num.intValue());
        Random rnd = new Random();
        int idx = rnd.nextInt(coPrimes.length);
        return coPrimes[idx];
    }

    /**
     * wildly inefficient...
     * @param newPrime the prime to find coprimes of
     * @return int array of all coprimes of newPrime
     */
    private int[] getCoprimeArray(int newPrime) {
        //list to store all coprimes in
        ArrayList<BigInteger> list = new ArrayList<BigInteger>();

        //start searching at one less than newPrime
        int i = newPrime - 1;
        BigInteger bigPrime = new BigInteger(newPrime + "");
        while (i > 0) {
            BigInteger bigrandom = new BigInteger("" + i);

            //if greatest common factor is 1, add to list
            if (bigrandom.gcd(bigPrime).equals(new BigInteger("1"))) {
                list.add(bigrandom);
            }
            i--;
        }
        //create and fill return int[]
        int[] out = new int[list.size()];
        for (int num = list.size() - 1; num >= 0; num--) {
            out[list.size() - 1 - num] = list.get(num).intValue();
        }
        return out;
    }

    /**
     * 
     * @param encryptedMessage message to decrypt
     * @return readable text
     */
    public String decryptFromPublic(String encryptedMessage){
        int[] message = fromString(encryptedMessage);
        String decryptedString = "";
        for(int i = 0; i < message.length; i++){
            BigInteger num = new BigInteger("" + message[i]);
            decryptedString +=  "" + num.modPow(new BigInteger("" + privateKey[0]), new BigInteger(""+ privateKey[1])).intValue();
        }
        return decryptedString;
    }

    /**
     * DOESNT WORK FIX Dycrypt exponent equation
     * generates private key
     * @param publicKey the public key this private key correlates to in the form [coPrime, modulus]
     */
    private void generatePrivateKey(int[] publicKey, BigInteger totient) {
        privateKey = new int[2];
        privateKey[1] = publicKey[1];
        for(int i = 2; i <= totient.intValue(); i++){
            if(((publicKey[0] * i) % totient.intValue()) == 1)
            {
              privateKey[0] =  i;
              return;
            }
          }
          
    }

    

    /**
     * converts string form of int[] to actual int[] datatype
     * @param string Array to string to be converted to int[]
     * @return int[] containing values contained in the string
     */
    private static int[] fromString(String string) {
        String[] strings = string.replace("[", "").replace("]", "").split(", ");
        int result[] = new int[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }
        return result;
    }


}