package com.librarysytsem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.TreeMap;

import com.librarysytsem.models.User;
import com.librarysytsem.models.Book;



public class SocketConnection {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String host;
    private final int port;

    public SocketConnection(String host, int port)   {
        this.host = host;
        this.port = port;
        connect();
    }

    private void connect() {
        
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendData(Object data) {
        try {
            outputStream.writeObject(data);
        } catch (IOException e) {
            System.out.println(e.getMessage() + " cant send data");
        }
    }

    public Object receiveData()  {
        try {
            return inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return e.getMessage() ; 
        }
    }

    public boolean available()   {
        try {
            return inputStream.available() > 0;
        } catch (IOException e) {
            System.out.println(e.getMessage());
           return false ; 
        }   
    }

    public void closeConnection()  {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
    }

    public void reconnect() {
        while (true) {
            try {
                connect();
                System.out.println("Connected to the server");
                break;
            } catch (Exception e) {
                System.out.println("Failed to connect to the server. Retrying in 5 seconds");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }
    }
}


class ServerConnection {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SocketConnection sc = new SocketConnection("localhost", 8000);

        HashMap<String,String> map = new HashMap<>();
        map.put("username", "1147");
        map.put("password", "mshaat");
        TreeMap<Integer, User> testMap = new TreeMap<>() ; 
        System.out.println(sc.receiveData());; 
        sc.sendData (map);
        
       
        Object data = sc.receiveData();
        System.out.println(data);
        data = sc.receiveData();
        System.out.println(data);
        TreeMap<Integer, User> receivedTreeMap = (TreeMap<Integer, User>) sc.receiveData();
        // System.out.println(receivedTreeMap);
        
           
        
            
                  

        
    }        
    } 
