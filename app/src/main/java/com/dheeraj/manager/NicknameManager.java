package com.dheeraj.manager;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class NicknameManager{

    int MIN = 3;
    int MAX = 17;
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[A-Za-z0-9_]+$");

    private final Set<String> reservedNicknames = ConcurrentHashMap.newKeySet();

    public boolean validateNickname(String nickName){
        if(nickName == null){
            return false;
        }
        String nickNameTrimmed = nickName.trim();
        int len = nickNameTrimmed.length();

        if(nickNameTrimmed.isEmpty()){
            return false;
        }
        if( MIN>len && len>MAX ){
            return false;
        }
        return ALLOWED_PATTERN.matcher(nickNameTrimmed).matches();
        
    }   
    public String normalizeNickName(String nickName){
        if(nickName==null){
            return "";
        }
        return nickName.trim().toLowerCase(Locale.ROOT);
    }
    public boolean reserveNickName(String nickName){
        if(!validateNickname(nickName)){
            return false;
        }
        String normalize = normalizeNickName(nickName);
        
        return reservedNicknames.add(normalize);

    }
    public void releaseNickName(String nickName){
        if(nickName != null && !nickName.trim().isEmpty()){
            reservedNicknames.remove(normalizeNickName(nickName));
        }
    }
    public boolean isNicknameAvaliable(String nickName){
        if(!validateNickname(nickName)) return false;
        return !reservedNicknames.contains(normalizeNickName(nickName));
    }
}