package de.jcup.asp.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.MapRequestParameterKey;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringRequestParameterKey;
import de.jcup.asp.core.Constants;
import de.jcup.asp.core.CryptoAccess;
import de.jcup.asp.core.CryptoAccess.DecryptionException;

public class AspClient {

    private static final Logger LOG = LoggerFactory.getLogger(AspClient.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;

    private CryptoAccess cryptoAccess;

    /**
     * Creates new ASP client.
     * @param base64EncodedKey key used to communicate encrypted with server. Must be same as from trusted server, otherwise no communication will be possible
     */
    public AspClient(String base64EncodedKey) {
        Objects.requireNonNull(base64EncodedKey, "key may not be null");
        if (base64EncodedKey.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.cryptoAccess = new CryptoAccess(base64EncodedKey);
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    
    public Response convertFile(Path adocfile, Map<String,Object> options) throws AspClientException {
        Request request = createRequest();
        
        request.set(StringRequestParameterKey.COMMAND, Commands.CONVERT_FILE);
        
        request.set(StringRequestParameterKey.SOURCE_FILEPATH, adocfile.toAbsolutePath().toString());
        request.set(MapRequestParameterKey.OPTIONS, options);
        
        return callServer(request);
       
    }
    
    @Deprecated // will be removed in future
    public Response resolveAttributes(File baseDir) throws AspClientException{
        Request request = createRequest();
        
        request.set(StringRequestParameterKey.COMMAND, Commands.RESOLVE_ATTRIBUTES_FROM_DIRECTORY);
        request.set(StringRequestParameterKey.BASE_DIR, baseDir.getAbsolutePath());

        return callServer(request);
        
    }
    
    public boolean isServerAlive() {
        Request request = createRequest();
        request.set(StringRequestParameterKey.COMMAND, Commands.IS_ALIVE);
        try {
            callServer(request);
            return true;
        } catch (NoConnectionAspClientException e) {
            return false;
        } catch (AspClientException e) {
            throw new IllegalStateException("Connection available but other error occurred:"+e.getMessage(),e);
        }
    }

    private Request createRequest() {
        Request request = new Request();
        request.set(StringRequestParameterKey.VERSION, "1.0"); 
        return request;
    }

    private Response callServer(Request r) throws AspClientException {
        Command command = r.getCommand();
        if (command==null) {
            throw new AspClientException("No command set");
        }
        
        try (Socket aspSocket = new Socket(InetAddress.getLoopbackAddress(), portNumber);
                PrintWriter out = new PrintWriter(aspSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(aspSocket.getInputStream()));) {
            
            String unencryptedRequestString = r.convertToString();
            LOG.debug("sending-unencrypted:\n{}",unencryptedRequestString);
            String encryptedRequestString = cryptoAccess.encrypt(unencryptedRequestString);
            
            out.println(encryptedRequestString);
            out.println(Request.TERMINATOR);
            
            String encryptedfromServer = null;
            StringBuilder result = new StringBuilder();
            while ((encryptedfromServer = in.readLine()) != null) {
                if (encryptedfromServer.equals(Response.TERMINATOR)) {
                    break;
                }
                LOG.debug("receiving-encrypted:{}",encryptedfromServer);
                String fromServer = cryptoAccess.decrypt(encryptedfromServer);
                LOG.debug("receiving-unencrypted:{}",fromServer);
                result.append(fromServer);
                result.append('\n');
            }
            
            Response response = Response.convertFromString(result.toString());
            if (response.failed()) {
                throw new AspClientException("Failed:"+response.getErrorMessage());
            }
            return response;
            
        } catch (Exception e) {
            if (e instanceof DecryptionException) {
                throw new FatalAspClientException("Crypto failure, normally untrusted ASP server", e);
            }
            if (e instanceof ConnectException) {
                throw new NoConnectionAspClientException("Connection to port "+portNumber+" refused", e);
            }
            throw new AspClientException("Command "+command.getId()+" failed.", e);
        }
    }

}
