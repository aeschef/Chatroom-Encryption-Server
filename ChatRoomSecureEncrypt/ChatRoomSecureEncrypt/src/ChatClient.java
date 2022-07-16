
//import statements
import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
public class ChatClient {

    // private instance variable
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int cesarShift = 24;
    private int[] serversKey;

    /**
     * Constructs Client Object and attempts to connect to server
     * @param ip to connect to
     * @param port to connect to
     */
    public ChatClient(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("<Client>\n Connection established to Server...");
        } catch (Exception e) {
            System.out.println("<Client>\n Error in constructor (line 30)" + e);
        }

    }

    public void main(){
        Scanner userInput = new Scanner(System.in);//move to ChatRoom! Pass in as a parameter?

        try{
            //recieve question from server
            String serverQ = in.readLine();
            System.out.println("<Client> \n" +serverQ);
            
            //let user respond using console
            out.println("y");
            
            // bad form... assumes server sent key and converts to send...
            serversKey = fromString(in.readLine());
  
            sendMessage(cesarShift + "");
             
            System.out.println("<Client> \n client received from server : " + decryptCesar(in.readLine()));

        }catch(IOException e){
            System.out.println("<Client> ERROR ABORT ABORT: " + e);
        }
        userInput.close();


    }

    /**
     * Encrypts and sends message to server
     * @param msg to be sent
     * 
     */
    public void sendMessage(String msg) {
        try {
            if(serversKey != null){
                msg = encryptMessage(msg);
            }
            out.println(msg);

            
        } catch (Exception e) {
            System.out.println("<Client>\n Error in sendMessage");
        }
    }

    /**
     * 
     * @param message
     * @return decrypted message
     */
    public String decryptCesar(String str){
        String result = "";
        char[] text = str.toCharArray();
        // traverse text
        for (int i=0;i<text.length;i++)
        {
            
            // apply transformation to each character
            // Encrypt Uppercase letters
            if (isupper(text[i])){
                result += (char)(((int)(text[i])+ (26 - cesarShift) -65)%26 +65);
            }
            else{
                result += (char)(((int)(text[i])+ (26 -cesarShift)-97)%26+97);
            }
        }
        // Return the resulting string
        return result;
    }

    public boolean isupper(char c)
    {
        return (int)c >= 65 && (int)c < 91;
    }
    /**
     * Returns String encrypted using public key sent by Server
     * @param encryptKey
     * @param message
     * @return
     */
    private String encryptMessage(String message){
        String encryptedMessage = "";
        String[] mess = new String[2];
        for(int l = 0; l < message.length(); l++){
            
            BigInteger temp = new BigInteger(message.charAt(l) + "");
            //System.out.println("init" + temp);
           
            BigInteger stuff = temp.modPow(new BigInteger("" + serversKey[0]) , new BigInteger("" +serversKey[1]));
            //System.out.println(stuff);
            mess[l] =  stuff.toString();
            
        }
        
        encryptedMessage = Arrays.toString(mess);
        return encryptedMessage;
    }

    /**
     * Severs connection with server
     */
    public void stopConnection() {
        try{
            in.close();
            out.close();
            clientSocket.close();
        }catch (IOException e){
            System.out.println("<Client>\nIOException thrown in stopConnection");
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
