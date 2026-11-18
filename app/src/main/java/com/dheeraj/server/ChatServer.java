package com.dheeraj.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {

    private static final int port = 5000;
    private static final  int poolSize = 10;
    private volatile boolean serverRunning = false;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private final AtomicInteger clientIdGenerator  = new AtomicInteger(0);
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    
    public void server(){
        System.out.println("==============================");
        System.out.println("      MINI CHAT SERVER");
        System.out.println("==============================");

        executor = Executors.newFixedThreadPool(poolSize);
        try {
            serverSocket = new ServerSocket(port);
            serverRunning = true;
            System.out.println("\nStarting server .....");
            System.out.println("\nServer listening on port: "+port);
            System.out.println("\n[SERVER] waiting for connections ...");
            while(serverRunning) {
                Socket clintSocket = serverSocket.accept();
                int clientId = clientIdGenerator.incrementAndGet();
                int currentActive = activeConnections.incrementAndGet();
                ClientHandler clientHandler = new ClientHandler(clintSocket,clientId,currentActive,this);
                executor.execute(clientHandler);
                
            }
        } catch (IOException e) {
            if (serverRunning) {
                System.err.println("Server error on Port " + port + ": " + e.getMessage());
            }
        } finally {
            shutdownServer();
        }
    }
    public void onClientDisconnected(int clientId){
        int remaining = activeConnections.decrementAndGet();
        System.out.println("\n[DISCONNECTED] Client ID: "+clientId+" | Active connections: "+remaining);
    }
    public void shutdownServer(){
        serverRunning = false;
        try {
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing ServerSocket: "+e.getMessage());
        }
        if(executor != null && !executor.isShutdown()){
            executor.shutdown();
        }
        System.out.println("Server shut down.");
    }
    
}