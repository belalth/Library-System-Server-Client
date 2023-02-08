package com.librarysytsem;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;


public class LibraryServer {
    public static void main(String[] args)  {
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
           System.out.println("Something Wrong with starting The server " + e.getMessage());
        }
       
    }
}


class HandleClient extends RWDatabase implements Runnable {
    private final Socket socket;
    private ObjectInputStream inputFromClient;
    private ObjectOutputStream outputToClient ;

    HandleClient(Socket socket) {
        this.socket = socket;
    }

    public void sendData(Object data)throws IOException {
        outputToClient.writeObject(data);
    }

    public Object receiveData() throws IOException, ClassNotFoundException {
        return inputFromClient.readObject();
    }

    public void closeConnection() throws IOException {
        outputToClient.close();
        inputFromClient.close();
        socket.close();
    }

    // HashMap<String, String> receivedMap = (HashMap<String, String>) receivedObject;

    @Override
    public void run()  {
        System.out.println("opened thread for client " + socket.getInetAddress().getHostName());
        

        try {
            inputFromClient = new ObjectInputStream(( socket).getInputStream());
            outputToClient = new ObjectOutputStream((socket).getOutputStream()) ;
           
            if (validate((HashMap<String,String>)inputFromClient.readObject())){
                // outputStream.writeLong(TokenAuth.generateToken());
                System.out.println("valid ");             
            }
            else{
                // outputStream.write(0);
                System.out.println("the password is wrong");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //check if the user in the database
    public boolean validate(HashMap<String,String> receivedMap){
        Integer username = Integer.parseInt(receivedMap.get("username")) ;
        String password = receivedMap.get("password") ;
        return UsersList.containsKey(username) && UsersList.get(username).getPassword().equals(password)  ;
    }
}


class TokenAuth {
    private static final LinkedList<Long> tokens = new LinkedList<>();
    static Long token  ;
    public static Long generateToken() {
        // Create a unique token based on the current time and username
        token = ((Long)System.currentTimeMillis()) * (57/12) ;
        // Store the token and the time it was generated in a map
        tokens.add(token);
        return token;
    }

    public static boolean isTokenValid(Long token){
        if (!tokens.contains(token) || token == 0 ) {
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