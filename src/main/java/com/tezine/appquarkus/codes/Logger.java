package com.tezine.appquarkus.codes;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    
    static public void logError(Exception e){
        System.out.println(e.getMessage());
    }

    static public void logError(String msg){
        System.out.println(ANSI_RED +msg+ANSI_RESET);
    }

    static public void logDebug(String msg){
        System.out.println(msg);
    }
}
