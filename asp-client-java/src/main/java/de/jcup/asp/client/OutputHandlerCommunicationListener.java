/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
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
