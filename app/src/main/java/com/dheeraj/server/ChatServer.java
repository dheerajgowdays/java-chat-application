package com.dheeraj.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.dheeraj.manager.ClientManager;
import com.dheeraj.manager.NicknameManager;

public class ChatServer {

    private static final int port = 5000;
    private static final  int poolSize = 10;

    private volatile boolean serverRunning = false;
    private ServerSocket serverSocket;
    private ExecutorService executor;

    private final NicknameManager nicknameManager = new NicknameManager();
    private final ClientManager clientManager = new ClientManager(nicknameManager);
    
    public void server(){
        System.out.println("==============================");
        System.out.println("      MINI CHAT SERVER");
        System.out.println("==============================");

        executor = Executors.newFixedThreadPool(poolSize);
        try {
            serverSocket = new ServerSocket(port);
            serverRunning = true;
            System.out.println("\n[SERVER] Initializing Chat Server...");
            System.out.println("[SERVER] Port        :"+port);
            System.out.println("[SERVER] Status      : RUNNING");
            System.out.println("[SERVER] Thread Pool :"+poolSize);
            System.out.println("\n[SERVER] waiting for connections ...");
            while(serverRunning) {
                Socket clintSocket = serverSocket.accept();                
                ClientHandler clientHandler = new ClientHandler(clintSocket,nicknameManager,clientManager);
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