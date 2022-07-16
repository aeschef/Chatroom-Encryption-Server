//import statements
import java.net.*;
import java.io.*;

public class ChatServer extends Thread {


    // instance variables
    private ServerSocket serverSocket;

    /**
     * Creates server object and establishes connection with Client
     * 
     * @param port
     */
    public  ChatServer(int port){
        try {
            System.out.println("<Server> \n Creating socket....");
            // creates socket port
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
           
            System.out.println("<Server> \nWaiting for client...");
            while(true){
                
                // accepts the client's request
                Socket client = serverSocket.accept();

                //Display new client
                System.out.println("<Server>\nNew client connected: "
                                   + client.getInetAddress()
                                         .getHostAddress());

                // create new thread object
                ClientHandler clientSock = new ClientHandler(client);

                //This thread handles the client seperately
                new Thread(clientSock).start();
            }

        } catch (IOException e) {
            e.printStackTrace();        
        }finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Closes server 
     * @throws IOException
     *
    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }*/


    


}
