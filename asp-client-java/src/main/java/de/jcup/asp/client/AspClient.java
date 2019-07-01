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
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.core.CryptoAccess.DecryptionException;
import de.jcup.asp.core.LogHandler;

public class AspClient {

    private static final Logger LOG = LoggerFactory.getLogger(AspClient.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;

    private CryptoAccess cryptoAccess;
    private OutputHandler outputHandler;
    private LogHandler logHandler;

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
            return getCancelResponse(r);
        }
        try (Socket aspSocket = new Socket(InetAddress.getLoopbackAddress(), portNumber);
                PrintWriter out = new PrintWriter(aspSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(aspSocket.getInputStream()));) {
            if (monitor.isCanceled()) {
                return getCancelResponse(r);
            }
            String unencryptedRequestString = r.convertToString();
            LOG.debug("sending-unencrypted:\n{}", unencryptedRequestString);
            String encryptedRequestString = cryptoAccess.encrypt(unencryptedRequestString);

            out.println(encryptedRequestString);
            out.println(Request.TERMINATOR);
            if (monitor.isCanceled()) {
                return getCancelResponse(r);
            }
            String encryptedfromServer = null;
            StringBuilder result = new StringBuilder();
            while ((encryptedfromServer = in.readLine()) != null) {
                if (monitor.isCanceled()) {
                    return getCancelResponse(r);
                }
                if (encryptedfromServer.equals(Response.TERMINATOR)) {
                    break;
                }
                LOG.debug("receiving-encrypted:{}", encryptedfromServer);
                String fromServer = cryptoAccess.decrypt(encryptedfromServer);
                LOG.debug("receiving-unencrypted:{}", fromServer);
                result.append(fromServer);
                result.append('\n');
            }
            if (monitor.isCanceled()) {
                return getCancelResponse(r);
            }
            Response response = Response.convertFromString(result.toString());
            if (response.failed()) {
                throw new AspClientException("Failed:" + response.getErrorMessage());
            }
            return response;

        } catch (Exception e) {
            if (e instanceof DecryptionException) {
                throw new FatalAspClientException("Crypto failure, normally untrusted ASP server", e);
            }
            if (e instanceof ConnectException) {
                throw new NoConnectionAspClientException("Connection to port " + portNumber + " refused", e);
            }
            throw new AspClientException("Command " + command.getId() + " failed.", e);
        }
    }
    
    private Response getCancelResponse(Request r) {
        Command command = r.getCommand();
        Response response = new Response();
        String errorMessage = null;
        if (command!=null) {
            errorMessage="Operation '"+command.getId()+"' was canceled by user";
        }else {
            errorMessage="Operation was canceled by user";
        }
        response.setErrorMessage(errorMessage);
        return response;
    }

}
