package de.jcup.asp.server.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Constants;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.core.CryptoAccess;
import de.jcup.asp.core.ExitCode;

import static de.jcup.asp.core.CoreConstants.SERVER_SECRET_OUTPUT_PREFIX;
import static de.jcup.asp.core.ServerExitCodes.*;

public class CoreAspServer {

    private static final Logger LOG = LoggerFactory.getLogger(CoreAspServer.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;
    private ClientRequestHandler requestHandler;

    private CryptoAccess cryptoAccess;
    private ServerSocket serverSocket;
    private boolean onExitKeepAlive;
    private ExitCode exitCode;
    
    public void setOnExitKeepAlive(boolean onExitKeepAlive) {
        this.onExitKeepAlive = onExitKeepAlive;
    }

    public boolean isReady() {
        return serverSocket!=null && serverSocket.isBound();
    }
    
    public CoreAspServer() {
        this.cryptoAccess=new CryptoAccess();
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    
    public void setRequestHandler(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
    
    public CryptoAccess getCryptoAccess() {
        return cryptoAccess;
    }

    public void start() {
        Objects.requireNonNull(requestHandler,"Request handler not set!");
        LOG.info("Server starting at port:{}", portNumber);
        LOG.info(SERVER_SECRET_OUTPUT_PREFIX+"{}",cryptoAccess.getSecretKey());

        try (ServerSocket serverSocket = new ServerSocket(portNumber,0,InetAddress.getLoopbackAddress())) {
            this.serverSocket=serverSocket;
            while (true) {
                try {
                    waitForClient();
                } catch (Exception e) {
                    LOG.error("Client communication failed", e);
                }
            }
        }catch(BindException be) {
            LOG.error("Already bind port:{}",portNumber,be);
            exit(ERROR_PORT_ALREADY_USED);
        } catch (Exception e) {
            LOG.error("Server cannot be started", e);
            exit(ERROR);
        }
        
    }
    
    protected void exit(ExitCode exitCode) {
        this.exitCode=exitCode;
        if (onExitKeepAlive) {
            LOG.info("Keep alive, exit:"+exitCode.toMessage());
        }else {
            System.exit(exitCode.getExitCode());
        }
    }
    
    /* read lines from client, until Request.TERMINATOR is send*/
    private void waitForClient() throws Exception{
        LOG.info("Server waiting for client call");
        try(Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            
            StringBuilder sb = new StringBuilder();
            String encryptedFromClient = null;
            while ( (encryptedFromClient=in.readLine())!=null){
                if (Request.TERMINATOR.equals(encryptedFromClient)) {
                    break;
                }
                String decryptedFromClient = cryptoAccess.decrypt(encryptedFromClient);
                sb.append(decryptedFromClient);
                sb.append('\n');
            }
            Response response =null;
            try {
                Request request = Request.convertFromString(sb.toString());
                if (Commands.IS_ALIVE.equals(request.getCommand())) {
                    /* we send back just an empty response*/
                    response=new Response();
                }else {
                    /* all other commands are handled by request handler*/
                    response = requestHandler.handleRequest(request);
                }
                
            }catch(Exception e) {
                LOG.error("Request handling failed", e);
                response=new Response();
                response.setErrorMessage(e.getMessage());
            }
            out.println(cryptoAccess.encrypt(response.convertToString()));
            out.println(Response.TERMINATOR);
            
        }
    }

    public boolean hasFailed() {
        return exitCode!= null && exitCode.getExitCode()!=0;
    }        
}
