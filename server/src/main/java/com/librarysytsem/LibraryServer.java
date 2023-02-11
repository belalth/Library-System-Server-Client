package com.librarysytsem;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import com.librarysytsem.database.Book;
import com.librarysytsem.database.User;



public class LibraryServer {
    private static final int PORT = 8000;
    protected static  Database Database =new Database() ;
    private static int clientNo = 0;

    public static void main(String[] args) {
        startServer();

    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("MultiThread server started at " + new Date());
            while (true) {
                Socket socket = serverSocket.accept();
                handleClient(socket);
            }
        } catch (IOException e) {
            System.out.println("Starting the server failed: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        InetAddress inetAddress = socket.getInetAddress();
        System.out.println("Client " + (++clientNo) + " connected with IP: " + inetAddress.getHostAddress() +
                ", Hostname: " + inetAddress.getHostName());
        new Thread(new HandleClient(socket)).start();
    }
}


class HandleClient extends LibraryServer implements Runnable {
    private static final long TOKEN_AUTH_ADMIN = 111L;
    private final Socket socket;
    private ObjectInputStream inputFromClient;
    private ObjectOutputStream outputToClient;

    HandleClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            outputToClient = new ObjectOutputStream(socket.getOutputStream());
            inputFromClient = new ObjectInputStream(socket.getInputStream());
            
            while(true){
                HashMap<String, String> data = (HashMap<String, String>) inputFromClient.readObject();
            
            if (data.get("username").equals("111") && data.get("password").equals("111")) {
                sendData(TOKEN_AUTH_ADMIN);
                sendData(true);
                sendData(true);
                sendGroupData();
                receiveWrite();
            } else if (data instanceof HashMap && validate(data)) {
                sendData(TokenAuth.generateToken());
                sendData(true);
                sendData(false);
                sendGroupData();
                receiveWrite();
            } else {
                sendData(0L);
                sendData(false);
                sendData(false);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void receiveWrite() throws ClassNotFoundException, IOException {
        Database.UsersList = (TreeMap<Integer, User>) inputFromClient.readObject();   
        Database.BooksList = (TreeMap<Integer, Book>) inputFromClient.readObject();
        Database.OwnedBooks = (HashMap<Integer, LinkedList<Book>>) inputFromClient.readObject(); 
        Database.writeBooksAndUsers();
        Database.writeOwnedBooks();
    }

    private void sendGroupData() throws IOException {
        sendData(Database.UsersList);   
        sendData(Database.BooksList);
        sendData(Database.OwnedBooks); 
    }

    private boolean validate(HashMap<String, String> receivedMap) {
        Integer username = Integer.parseInt(receivedMap.get("username"));
        String password = receivedMap.get("password");
        return Database.UsersList.containsKey(username) && 
               Database.UsersList.get(username).getPassword().equals(password);
    }

    private void sendData(Object data) throws IOException {
        outputToClient.writeObject(data);
        outputToClient.flush();
    }

    private void closeConnection() {
        try {
            outputToClient.close();
            inputFromClient.close();
            socket.close();
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