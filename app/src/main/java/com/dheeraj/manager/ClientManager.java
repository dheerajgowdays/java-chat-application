package com.dheeraj.manager;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.dheeraj.server.ClientHandler;

public class ClientManager{

    private final ConcurrentHashMap<String,ClientHandler> clients = new ConcurrentHashMap<>();
    private final NicknameManager nicknameManager;

    public ClientManager(NicknameManager nicknameManager ){
        this.nicknameManager = nicknameManager;
    }

    public boolean registerClient( String rawName , ClientHandler handler){
        if(handler == null || !nicknameManager.reserveNickName(rawName)){
            return false;
        }
        String normalizd = nicknameManager.normalizeNickName(rawName);
        if(clients.putIfAbsent(rawName, handler)!= null){
            nicknameManager.releaseNickName(normalizd);
            return false;
        }
        return true;
    }

    public void removeClient(String nickname, ClientHandler handler){
        if(nickname == null || handler == null){
            return;
        }
        String normalized = nicknameManager.normalizeNickName(nickname);
        if(clients.remove(normalized, handler)){
            nicknameManager.releaseNickName(normalized);
        }
    }

    public ClientHandler getClient(String nickname){
        if(nickname == null || nickname.trim().isEmpty()){
            return null;
        }
        return clients.get(nicknameManager.normalizeNickName(nickname));
    }

    public Set<String> getOnlineUsers(){
        return Collections.unmodifiableSet(new HashSet<>(clients.keySet()));
    }

    public int getClientCount(){
        return clients.size();
    }

    public Collection<ClientHandler> getAllClients(){
        return Collections.unmodifiableCollection(clients.values());
    }

    public void notifyAllExcept(String message,ClientHandler exclude) throws IOException{
        for(ClientHandler client : clients.values()){
            if(client != exclude){
                client.sendMessage(message);
            }
        }
    }

    
    
}