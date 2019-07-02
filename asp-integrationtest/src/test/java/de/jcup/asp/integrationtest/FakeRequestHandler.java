package de.jcup.asp.integrationtest;

import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.server.core.ClientRequestHandler;

public class FakeRequestHandler implements ClientRequestHandler{

    private int millisecondsToWaitForResult=0;
    private Response response=new Response();
    
    public FakeRequestHandler(){
        
    }
    
    public FakeRequestHandler allRequestsNeedsOnServerSide(int milliseconds) {
        this.millisecondsToWaitForResult=milliseconds;
        return this;
    }
    
    /**
     * Setup new response to be returned on next calls
     * @return created new response
     */
    protected Response response() {
        this.response=new Response();
        return response;
    }
    
    
    @Override
    public Response handleRequest(Request request) {
        if (millisecondsToWaitForResult>0) {
            try {
                Thread.sleep(millisecondsToWaitForResult);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
        }
        return response;
    }

    public void reset() {
        response();
    }
    
    public void allRequestsErrorMessage(String message) {
        response().setErrorMessage(message);
    }
    
}