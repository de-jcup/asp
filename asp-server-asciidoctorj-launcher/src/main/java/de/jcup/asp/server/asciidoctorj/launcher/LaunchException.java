package de.jcup.asp.server.asciidoctorj.launcher;

public class LaunchException extends Exception{

    private static final long serialVersionUID = 1L;
    
    public LaunchException(String message, Throwable t) {
        super(message,t);
    }

}
