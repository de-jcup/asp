package de.jcup.asp.core;

public enum ServerExitCodes implements ExitCode{
    OK(0,"OK"),
    
    ERROR_PORT_ALREADY_USED(10,"Port already in use!"),
    
    ERROR(1,"Some common error happened"),
    ;
    private int exitCode;
    private String description;
    
    private ServerExitCodes(int exitCode, String description) {
        this.exitCode=exitCode;
        this.description=description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getExitCode() {
        return exitCode;
    }

    public static ExitCode from(int exitCode) {
        for (ExitCode code: ServerExitCodes.values()) {
            if (code.getExitCode()==exitCode) {
                return code;
            }
        }
        return new UnknownExitCode(exitCode);
    }
}
