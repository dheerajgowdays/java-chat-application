package com.dheeraj.manager;

import java.util.Locale;
import java.util.regex.Pattern;

public class NicknameManager{

    int MIN = 3;
    int MAX = 17;
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[A-Za-z0-9_]");

    public boolean validateNickname(String nickName){

        String nickNameTrimmed = nickName.trim();
        int len = nickNameTrimmed.length();

        if(nickNameTrimmed == null){ 
            return false;
        }
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
        return true;

    }
}