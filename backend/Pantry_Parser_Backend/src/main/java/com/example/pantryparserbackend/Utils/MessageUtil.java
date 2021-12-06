package com.example.pantryparserbackend.Utils;

public class MessageUtil {
    public static String newResponseMessage(boolean success, String message){
        return "{\"success\":\"" + success + "\"," + " \"message\":\"" + message + "\"}";
    }
}
