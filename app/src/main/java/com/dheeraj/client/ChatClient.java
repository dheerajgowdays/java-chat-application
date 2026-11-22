package com.dheeraj.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClient {
    Scanner sc;
    public ChatClient(Scanner sc) {
        this.sc = sc;
    }
    public void client() {
        System.out.println("======================");
        System.out.println("   MINI CHAT CLIENT");
        System.out.println("======================");
        int port = 5000;
        try{
            System.out.println("\nConnecting to server...");
            Socket socket = new Socket("localHost",port);
            System.out.println("localhost:"+port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            
            Thread listenerThread = new Thread(() ->{
                try {
                    String line;
                    while((line = reader.readLine()) != null){
                        System.out.println(line);
                    }
                } catch (IOException ignored) {
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

            while(sc.hasNextLine()){
                System.out.print("\n> ");
                String message = sc.nextLine();
                if(message.equals("/quit")){
                    writer.write("Goodbye!");
                    writer.newLine();
                    writer.flush();
                    break;
                }
                writer.write(message);
                writer.newLine();
                writer.flush();
                String serverResponse = reader.readLine();
                if(serverResponse == null){
                    System.out.println("Server closed the connection");
                }
            }
        } catch (IOException e) {
            System.err.println("Connection failed By: "+e.getMessage());
        }
    }
}