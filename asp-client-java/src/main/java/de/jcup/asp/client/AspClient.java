package de.jcup.asp.client;

import java.io.File;
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
import de.jcup.asp.core.LogHandler;
import de.jcup.asp.core.OutputHandler;

public class AspClient {

    private static final Logger LOG = LoggerFactory.getLogger(AspClient.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;
    private CryptoAccess cryptoAccess;
    private OutputHandler outputHandler;
    private LogHandler logHandler;
    private AspClientProgressMonitorSurveillance progressMonitorSurveillance;
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
        this.progressMonitorSurveillance=AspClientProgressMonitorSurveillance.INSTANCE;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    
    public void setOutputHandler(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }
    
    public void setLogHandler(LogHandler logHandler) {
        this.logHandler = logHandler;
    }
    
    public int getPortNumber() {
        return portNumber;
    }

    public Response convertFile(Path adocfile, Map<String, Object> options, AspClientProgressMonitor monitor ) throws AspClientException {
        Request request = createRequest();

        request.set(StringRequestParameterKey.COMMAND, Commands.CONVERT_FILE);

        request.set(StringRequestParameterKey.SOURCE_FILEPATH, adocfile.toAbsolutePath().toString());
        request.set(MapRequestParameterKey.OPTIONS, options);

        return callServer(request,monitor);

    }

    @Deprecated // will be removed in future
    public Response resolveAttributes(File baseDir, AspClientProgressMonitor monitor) throws AspClientException {
        Request request = createRequest();

        request.set(StringRequestParameterKey.COMMAND, Commands.RESOLVE_ATTRIBUTES_FROM_DIRECTORY);
        request.set(StringRequestParameterKey.BASE_DIR, baseDir.getAbsolutePath());

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
        request.set(StringRequestParameterKey.VERSION, "1.0");
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
            return AspClientCall.createCancelResponse(r);
        }
        AspClientCall cr = new AspClientCall(this, r,monitor);
        
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

    public String encrypt(String unencryptedRequestString) {
        return cryptoAccess.encrypt(unencryptedRequestString);
    }
    
    public String decrypt(String unencryptedRequestString) throws DecryptionException {
        return cryptoAccess.decrypt(unencryptedRequestString);
    }
    
    
    

}
