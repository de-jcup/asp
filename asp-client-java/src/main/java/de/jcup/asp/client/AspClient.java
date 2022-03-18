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

import java.nio.file.Path;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.MapRequestParameterKey;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringRequestParameterKey;
import de.jcup.asp.api.asciidoc.AsciidocAttributes;
import de.jcup.asp.api.asciidoc.AsciidocOptions;
import de.jcup.asp.core.Constants;
import de.jcup.asp.core.CryptoAccess;
import de.jcup.asp.core.LogHandler;
import de.jcup.asp.core.OutputHandler;

public class AspClient {

    private static final Logger LOG = LoggerFactory.getLogger(AspClient.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;
    private CryptoAccess cryptoAccess;
    private OutputHandler outputHandler;
    private LogHandler logHandler;
    private boolean showCommunication;
    private AspClientProgressMonitorSurveillance progressMonitorSurveillance;
    private OutputHandlerCommunicationListener outputHandlerCommunicationListener;

    /**
     * Creates new ASP client.
     * 
     * @param base64EncodedKey key used to communicate encrypted with server. Must
     *                         be same as from trusted server, otherwise no
     *                         communication will be possible
     */
    public AspClient(String base64EncodedKey) {
        Objects.requireNonNull(base64EncodedKey, "key may not be null");
        if (base64EncodedKey.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.cryptoAccess = new CryptoAccess(base64EncodedKey);
        this.outputHandlerCommunicationListener=new OutputHandlerCommunicationListener();
        this.progressMonitorSurveillance=AspClientProgressMonitorSurveillance.INSTANCE;
    }

    /**
     * When enabled communication output is shown in output handler
     * @param show
     */
    public void setShowCommunication(boolean show) {
        this.showCommunication = show;
    }
    
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    
    public void setOutputHandler(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
        outputHandlerCommunicationListener.setOutputHandler(outputHandler);
    }
    
    public void setLogHandler(LogHandler logHandler) {
        this.logHandler = logHandler;
    }
    
    public int getPortNumber() {
        return portNumber;
    }

    public Response convertFile(Path adocfile, AsciidocOptions asciidocOptions, AsciidocAttributes asciidocAttributes, AspClientProgressMonitor monitor ) throws AspClientException {
        Request request = createRequest();
        if (asciidocOptions==null) {
            asciidocOptions = AsciidocOptions.builder().build();
        }
        if (asciidocAttributes==null) {
            asciidocAttributes = AsciidocAttributes.builder().build();
        }

        request.set(StringRequestParameterKey.COMMAND, Commands.CONVERT_FILE);

        request.set(StringRequestParameterKey.SOURCE_FILEPATH, adocfile.toAbsolutePath().toString());
        request.set(MapRequestParameterKey.OPTIONS, asciidocOptions.toMap());
        request.set(MapRequestParameterKey.ATTRIBUTES, asciidocAttributes.toMap());

        return callServer(request,monitor);

    }

    public boolean isServerAlive(AspClientProgressMonitor monitor ) {
        Request request = createRequest();
        request.set(StringRequestParameterKey.COMMAND, Commands.IS_ALIVE);
        try {
            callServer(request,monitor);
            return true;
        } catch (NoConnectionAspClientException e) {
            return false;
        } catch (AspClientException e) {
            String message = "Connection possible, but:"+e.getMessage();
            if (outputHandler!=null) {
                outputHandler.output(message);
            }
            if (logHandler!=null) {
                logHandler.error(message, e);
            }else {
                LOG.error(message, e);
            }
            return false;
        }
    }

    private Request createRequest() {
        Request request = new Request();
        request.set(StringRequestParameterKey.VERSION, Version.getVersion());
        return request;
    }

    private Response callServer(Request r, AspClientProgressMonitor monitor) throws AspClientException {
        if (monitor==null) {
            monitor = NullAspClientProgressMonitor.NULL_PROGRESS;
        }
        Command command = r.getCommand();
        if (command == null) {
            throw new AspClientException("No command set");
        }
        if (monitor.isCanceled()) {
            return AspClientAction.createCancelResponse(r);
        }
        AspClientCommunicationListener listener = null;
        if (showCommunication) {
            listener =outputHandlerCommunicationListener;
        }
        AspClientAction cr = new AspClientAction(cryptoAccess, portNumber, listener, r,monitor);
        
        progressMonitorSurveillance.inspect(cr,0);
        
        cr.run();
      
        if (cr.getException()!=null) {
            throw cr.getException();
        }
        if (cr.getResponse()==null) {
            throw new AspClientException("Unhandled request");
        }
        return cr.getResponse();
    }

}
