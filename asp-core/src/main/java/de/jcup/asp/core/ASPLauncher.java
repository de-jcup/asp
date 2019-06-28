package de.jcup.asp.core;

public interface ASPLauncher {

    /**
     * Launch ASP on given port and returns server secret
     * @param port
     * @return secret server key
     */
    public String launch(int port) throws LaunchException;
}
