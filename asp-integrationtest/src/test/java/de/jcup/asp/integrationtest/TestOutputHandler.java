package de.jcup.asp.integrationtest;

import java.util.ArrayList;
import java.util.List;

import de.jcup.asp.core.OutputHandler;

public class TestOutputHandler implements OutputHandler{

    private List<String> messages = new ArrayList<String>();
    
    @Override
    public void output(String message) {
        messages.add(message);
        System.out.println(">> test-outputhandler:"+message);
        
    }
    
    public List<String> getMessages() {
        return messages;
    }

}
