package com.librarysytsem;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;




public class LibraryServer extends Database {
    //start loading books/users data from database
    //check everything is loaded correctly
    public static LibraryServer Server ;
    LibraryServer(){
        super();
    }
    public static void main(String[] args) {
        Server = new LibraryServer();
        Server.startServer();
    }

    public void startServer()  {        
        int clientNo = 0;
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("MultiThread server started at " + new Date());
            

            while (true) {
                Socket socket = serverSocket.accept(); 
                clientNo++;
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("Client " + clientNo + " connected with IP: " + inetAddress.getHostAddress() +
                        " , Hostname: " + inetAddress.getHostName());

                new Thread(new HandleClient(socket)).start();
            }
        }
        catch (IOException e) {
           System.out.println("STARTING THE SERVER FAILED \n" + e.getMessage());
        }
       
    }
}


class HandleClient extends LibraryServer implements Runnable {
    private final Socket socket;
    private ObjectInputStream inputFromClient = null ;
    private ObjectOutputStream outputToClient = null ;

    HandleClient(Socket socket) {
        this.socket = socket;
        try {
            inputFromClient = new ObjectInputStream(( socket).getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            outputToClient = new ObjectOutputStream((socket).getOutputStream()) ;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run(){

        while (true){

            if (!validate((HashMap<String,String>) receiveData())) {
                System.out.println("not valid"); 
                sendData((Long)0L);
                flushOutput();
                sendData(false);
                flushOutput();
                

            } else {
                System.out.println("valid ");
                sendData(TokenAuth.generateToken());
                flushOutput();
                sendData(true);
                flushOutput();
                break; 
            } 
        }
    }
    //check if the user in the database
    public boolean validate(HashMap<String,String>  receivedMap){
        Integer username = Integer.parseInt(receivedMap.get("username")) ;
        String password = receivedMap.get("password") ;
        return Server.UsersList.containsKey(username) && Server.UsersList.get(username).getPassword().equals(password)  ;
    }

    public void sendData(Object data){
        try {
            outputToClient.writeObject(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Object receiveData()  {
        try {
            return inputFromClient.readObject();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        }
        return inputFromClient ;  
    }

    public void closeConnection()   {
        try {
            outputToClient.close();
            inputFromClient.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
    }
    public void flushOutput()  {
        try {
            outputToClient.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    } 
       
}    

/**
 * TokenAuth
 */
class TokenAuth {
    private static final LinkedList<Long> tokens = new LinkedList<>();
    static Long genTok  ;
    public static Long generateToken() {
        // Create a unique token based on the current time and username
        genTok = ((Long)System.currentTimeMillis()) * (57/12) ;
        // Store the token and the time it was generated in a map
        tokens.add(genTok);
        return genTok;
    }

    public static boolean isTokenValid(Long token){
        if (!tokens.contains(token) || token == 0L ) {
            // If the token doesn't exist, it's not valid
            return false;
        }
        // Check if the token has expired (1 hour in this example)
        if (System.currentTimeMillis() - ((Long)token *12/57 ) > 3600 * 1000) {
            tokens.remove(token);
            return false;
        }
        return true;
    }
}