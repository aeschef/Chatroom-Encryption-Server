public class ChatRoom{
    public static void main(String[] args) throws Exception {
        System.out.println("<Room> \nWelcome to ChatRoom!");
        Thread serverThread = new Thread(new Runnable(){
            public void run(){
                ChatServer server = new ChatServer(8888); 
                
            }
        });
        serverThread.start();
        System.out.println("<Room> \nserver object created");

        Thread clientThread = new Thread(new Runnable() {
            public void run(){
                //creates and connects a client to the server...
                ChatClient client1 = new ChatClient("127.0.0.1", 8888);
                client1.main();
                //creates and connects a client to the server...
                ChatClient client2 = new ChatClient("127.0.0.1", 8888);
                client2.main();

            }
        });
        clientThread.start();
        System.out.println("<Room> \n Clients started");

        //set up multithreads
        
        
    }
}

   /**
     * Deprecated... Move to ChatRoom.java eventually...
     */
    /*
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        Client client = new Client("127.0.0.1", 6666);
        try {
            //send message to server and print response
            client.out.println("hello server");
            String response = client.in.readLine();
            System.out.println(response);

            //let user respond using console
            client.out.println(userInput.nextLine());

            //technically bad form... assumes that server sent public key and converts to individual numbers
            client.serversKey = fromString(client.in.readLine());
  
            client.sendMessage("24");

        } catch (Exception e) {
            System.out.println("Error in client Main: " + e);
        }

        userInput.close();
    }*/

   /**
     * Deprecated.... Move To ChatRoom.java Eventually
     * 
     */
    /* 
    public static void main(String[] args) {

        

        try {
            System.out.println(server.in.readLine()); // 'hello server'

            server.out.println("Enter 'y' if you would like a public key");
            String wouldLikeKey = server.in.readLine();
            if () {
               
                
            }

            //recieves message... sent to nonexistent method to be decrypted using private keys stored in file
            String toDecrypt = server.in.readLine();
            System.out.println(server.decryptFromPublic(toDecrypt));
        } catch (Exception e) {
            System.out.println("server error in main" + e);
        }
    }
    */
