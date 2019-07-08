package de.jcup.asp.client;

import de.jcup.asp.core.OutputHandler;

/**
 * This listener does log communication as debug logs and also to output handlers
 *
 */
public class OutputHandlerCommunicationListener implements AspClientCommunicationListener {

    private OutputHandler outputHandler;

    public OutputHandlerCommunicationListener() {
        
    }
    
    public void setOutputHandler(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }
    
    @Override
    public void sending(String toServer) {
        if (outputHandler!=null) {
            outputHandler.output("ASP client->server :"+toServer);
        }
        
    }

    @Override
    public void receiving(String fromServer) {
        if (outputHandler!=null) {
            outputHandler.output("ASP server->client :"+fromServer);
        }
    }

}
