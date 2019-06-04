package de.jcup.asp.server.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Constants;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;

public class AspServer {

    private static final Logger LOG = LoggerFactory.getLogger(AspServer.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;
    private ClientRequestHandler requestHandler;

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    
    public void setRequestHandler(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void start() {
        Objects.requireNonNull(requestHandler,"Request handler not set!");
        LOG.info("Server starting at port:{}", portNumber);

        try (ServerSocket serverSocket = new ServerSocket(portNumber,0,InetAddress.getLoopbackAddress())) {
            while (true) {
                try {
                    waitForClient(serverSocket);
                } catch (Exception e) {
                    LOG.error("Client communication failed", e);
                }
            }
        } catch (Exception e) {
            LOG.error("Server cannot be started", e);
        }
        
    }

    private void waitForClient(ServerSocket serverSocket) throws Exception{
        LOG.info("Server starting for client call");
        try(Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            
            StringBuilder sb = new StringBuilder();
            String fromClient = null;
            while ( (fromClient=in.readLine())!=null){
                if (Request.TERMINATOR.equals(fromClient)) {
                    break;
                }
                sb.append(fromClient);
                sb.append('\n');
            }
            Response response =null;
            try {
                Request request = Request.convertFromString(sb.toString());
                response = requestHandler.handleRequest(request);
                
            }catch(Exception e) {
                LOG.error("Request handling failed", e);
                response=new Response();
                response.setErrorMessage(e.getMessage());
            }
            out.println(response.convertToString());
            out.println(Response.TERMINATOR);
            
        }
    }        
}