package de.jcup.asp.api;

public class ProtocolDataException extends Exception{

    private static final long serialVersionUID = -8048451759576441896L;
    
    public ProtocolDataException(String message, Throwable reason) {
        super(message,reason);
    }
    
    @Override
    public String getMessage() {
        String message = super.getMessage();
        Throwable t = getCause();
        if (t!=null) {
            message+=", reason:"+t.getMessage();
        }
        return message;
    }
}