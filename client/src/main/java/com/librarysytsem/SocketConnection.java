package com.librarysytsem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.chrono.IsoChronology;

public class SocketConnection {
    Socket socket ;
    ObjectInputStream inputStream ;
    ObjectOutputStream outputStream ;

    SocketConnection(String ip , int port ) throws IOException {
         socket = new Socket(ip , port );
         inputStream = new ObjectInputStream(socket.getInputStream());
         outputStream = new ObjectOutputStream(socket.getOutputStream()) ;
    }

    public void sendData(Object data)throws IOException {
        outputStream.writeObject(data);
    }

    public Object receiveData() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }

    public void closeConnection() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}


class Testing{
    public static void main(String[] args) throws IOException {
        SocketConnection socketConnection = new SocketConnection("localhost" , 8000);




    }








}