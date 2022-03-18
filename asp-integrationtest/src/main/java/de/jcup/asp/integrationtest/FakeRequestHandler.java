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