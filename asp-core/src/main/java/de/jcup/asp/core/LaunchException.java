package de.jcup.asp.core;

public class LaunchException extends Exception{

    private static final long serialVersionUID = 1L;
    
    public LaunchException(String message) {
        super(message);
    }
    
    public LaunchException(String message, Throwable t) {
        super(message,t);
    }

}
