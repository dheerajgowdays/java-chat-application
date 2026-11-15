package com.dheeraj.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("\nClient connected !");
                    System.out.println("\nClient IP :" + clientSocket.getInetAddress());
                    System.out.println("Client Port :" + clientSocket.getPort());
                    System.out.println("\nConnection Established");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
                    String clientMessage;
                    while((clientMessage = bufferedReader.readLine()) != null) {
                        if (clientMessage.equals("/quit")) {
                            System.out.println("\n Client: "+clientMessage);
                            System.out.println("\nConnection closed");
                            break;
                        }
                        System.out.println("\n Received: " + clientMessage);
                        String response = "Message Received Successfully";
                        System.out.println("\nResponse: " + response);
                        bufferedWriter.write(response);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                }catch (IOException e) {
                    System.err.println("Client Connection Close :"+e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error on Port "+port+": "+e.getMessage());
        }
    }
    
}