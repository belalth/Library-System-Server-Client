package com.librarysytsem;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class LibraryServer {
    public static void main(String[] args) {
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
        } catch (IOException e) {
            System.err.println("An error occurred while starting the server: " + e.getMessage());
        }
    }
}


class HandleClient extends RWDatabase implements Runnable {
    private final Socket socket ;

   HandleClient(Socket socket){
       this.socket = socket ;
   }

    @Override
    public void run() {
        System.out.println("opened thread for client " + socket.getInetAddress().getHostName());
    }
}
