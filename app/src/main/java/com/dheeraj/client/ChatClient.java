package com.dheeraj.client;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    public void client() {
        System.out.println("======================");
        System.out.println("   MINI CHAT CLIENT");
        System.out.println("======================");
        int port = 5000;
        try{
            System.out.println("\nConnecting to server...");
            Socket socket = new Socket("localHost",port);
            System.out.println("\nHost: "+socket.getInetAddress().getHostName());
            System.out.println("Port: "+socket.getPort());
            System.out.println("\nConnected Successfully");
            System.out.println("\nConnection established with server");
        } catch (IOException e) {
            System.err.println("Connection failed By: "+e.getMessage());
        }
    }
}