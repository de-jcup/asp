package de.jcup.asp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.core.CryptoAccess.DecryptionException;

class AspClientCall implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(AspClientCall.class);
    private Response response;
    private Request r;
    private AspClientProgressMonitor progressMonitor;
    private AspClientException exception;
    private AspClient client;

    private Socket aspSocket;

    private AspClientCommunicationListener communicationListener;

    AspClientCall(AspClient client, AspClientCommunicationListener listener, Request r, AspClientProgressMonitor monitor) {
        this.client = client;
        this.communicationListener=listener;
        this.r = r;
        this.progressMonitor = monitor;
    }

    @Override
    public void run() {
        try {
            response = internalCallServer(r, progressMonitor);
        } catch (AspClientException e) {
            this.exception = e;
        }
    }

    public AspClientProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    public AspClientException getException() {
        return exception;
    }

    public Response getResponse() {
        return response;
    }

    public boolean done() {
        return response != null || this.exception != null;
    }

    public void cancel() throws IOException {
        if (aspSocket == null) {
            return;
        }
        if (aspSocket.isClosed()) {
            return;
        }
        aspSocket.close();
    }

    private Response internalCallServer(Request r, AspClientProgressMonitor monitor) throws FatalAspClientException, NoConnectionAspClientException, AspClientException {
        Command command = r.getCommand();
        int portNumber = client.getPortNumber();
        try (Socket aspSocket = new Socket(InetAddress.getLoopbackAddress(), portNumber);
                PrintWriter out = new PrintWriter(aspSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(aspSocket.getInputStream()));) {
            this.aspSocket = aspSocket;
            if (monitor.isCanceled()) {
                return createCancelResponse(r);
            }
            String unencryptedRequestString = r.convertToString();
            if (communicationListener!=null) {
                communicationListener.sending(unencryptedRequestString);
            }
            String encryptedRequestString = client.encrypt(unencryptedRequestString);

            out.println(encryptedRequestString);
            out.println(Request.TERMINATOR);
            if (monitor.isCanceled()) {
                return createCancelResponse(r);
            }
            String encryptedfromServer = null;
            StringBuilder result = new StringBuilder();
            while ((encryptedfromServer = in.readLine()) != null) {
                if (monitor.isCanceled()) {
                    return createCancelResponse(r);
                }
                if (encryptedfromServer.equals(Response.TERMINATOR)) {
                    break;
                }
                LOG.debug("receiving-encrypted:{}", encryptedfromServer);
                String fromServer = client.decrypt(encryptedfromServer);
                if (communicationListener!=null) {
                    communicationListener.receiving(fromServer);
                }
                result.append(fromServer);
                result.append('\n');
            }
            if (monitor.isCanceled()) {
                return createCancelResponse(r);
            }
            Response response = Response.convertFromString(result.toString());
            if (response.failed()) {
                throw new AspClientException("Failed:" + response.getErrorMessage());
            }
            return response;

        } catch (Exception e) {
            if (monitor.isCanceled()) {
                return AspClientCall.createCancelResponse(r);
            }
            if (e instanceof DecryptionException) {
                throw new FatalAspClientException("Crypto failure, normally untrusted ASP server", e);
            }else  if (e instanceof ConnectException) {
                throw new NoConnectionAspClientException("Connection to port " + portNumber + " refused", e);
            }else {
                throw new AspClientException("Command " + command.getId() + " failed.", e);
            }
        }
    }

    public static Response createCancelResponse(Request r) {
        Command command = r.getCommand();
        Response response = new Response();
        String errorMessage = null;
        if (command != null) {
            errorMessage = "Operation '" + command.getId() + "' was canceled by user";
        } else {
            errorMessage = "Operation was canceled by user";
        }
        response.setErrorMessage(errorMessage);
        return response;
    }

}