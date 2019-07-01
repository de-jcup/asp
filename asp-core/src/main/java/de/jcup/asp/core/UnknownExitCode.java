package de.jcup.asp.core;

public class UnknownExitCode implements ExitCode {

    private int exitCode;
    
    public UnknownExitCode(int exitCode) {
        this.exitCode=exitCode;
    }
    
    @Override
    public String getDescription() {
        return "Unknown exit code";
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
