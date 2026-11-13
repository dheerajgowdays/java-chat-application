package com.dheeraj.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public void server(){
        System.out.println("==============================");
        System.out.println("      MINI CHAT SERVER");
        System.out.println("==============================");

        int port = 5000;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("\nStarting server .....");
            System.out.println("\nServer listening on port: "+port);
            System.out.println("\nWaiting for client connection ....");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nClient connected !");
                System.out.println("\nClient IP :"+clientSocket.getInetAddress());
                System.out.println("Client Port :"+clientSocket.getPort());
                System.out.println("\nConnection Established");
            }
        } catch (IOException e) {
            System.err.println("Server error on Port "+port+": "+e.getMessage());
        }
    }
    
}