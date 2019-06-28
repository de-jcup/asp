package de.jcup.asp.core;

public interface ExitCode {

    public String getDescription();
    
    public int getExitCode() ;
    
    public default String toMessage() {
        return getDescription()+":"+getExitCode();
    }
}
