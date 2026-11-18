package com.dheeraj.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable{

    private final Socket socket;
    private final int clientId;
    private final int activeSanpshot;
    private final ChatServer server;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ClientHandler(Socket socket,int clientId, int activeSnapshot, ChatServer server) {
        this.socket = socket;
        this.clientId = clientId;
        this.activeSanpshot = activeSnapshot;
        this.server = server;
    }

    @Override
    public void run() {

        String threadName = Thread.currentThread().getName();
        String remoteIp = socket.getInetAddress().getHostAddress();

        System.out.println("\n[NEW CONNECTION]");
        System.out.println("Client ID: "+clientId);
        System.out.println("Remote Address: "+remoteIp);
        System.out.println("Handler: "+threadName);
        System.out.println("\nActive connections: "+activeSanpshot);

        String clientInfo = socket.getInetAddress() + ":" + socket.getPort();
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            String clientMessage;
            while((clientMessage=reader.readLine())!= null){
                if(clientMessage.equalsIgnoreCase("/quit")){
                    break;
                }
                System.out.println("\n[Client "+clientId+"] "+clientMessage);
                sendMessage("Message Received Successfully");
            }
        } catch (IOException e) {
            System.err.println("\n[Client " + clientId + "] Connection lost: " + e.getMessage());
        } finally {
            closeConnection();
            server.onClientDisconnected(clientId);
        }
    }
    public void sendMessage(String message) throws IOException{
        if(writer != null){
            writer.write(message);
            writer.newLine();
            writer.flush();
        }
    }

    public void closeConnection(){
        try{
            if(reader != null){
                reader.close();
            }
        }catch(IOException e){
            System.err.println("Error closing reader: "+e.getMessage());
        }
        try {
            if(writer != null){
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing writer: "+e.getMessage());
        }

        try {
            if(socket != null && !socket.isClosed()){
                socket.close();
                System.out.println("Resource released for client: "+socket.getInetAddress()+":"+socket.getPort());
            }
        } catch (IOException e) {
        }
    }
}