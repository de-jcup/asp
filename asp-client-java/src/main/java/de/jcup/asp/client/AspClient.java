package de.jcup.asp.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Constants;
import de.jcup.asp.api.MapRequestParameterKey;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringRequestParameterKey;

public class AspClient {

    private static final Logger LOG = LoggerFactory.getLogger(AspClient.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    
    private Request createRequest() {
        Request request = new Request();
        request.set(StringRequestParameterKey.VERSION, "1.0"); 
        return request;
    }

    public Response convertFile(Path adocfile, Map<String,Object> options) throws AspClientException {
        Request request = createRequest();
        
        request.set(StringRequestParameterKey.COMMAND, Commands.CONVERT_FILE);
        
        request.set(StringRequestParameterKey.SOURCE_FILEPATH, adocfile.toAbsolutePath().toString());
        request.set(MapRequestParameterKey.OPTIONS, options);
        
        return callServer(request);
       
    }
    
    public Response resolveAttributes(File baseDir) throws AspClientException{
        Request request = createRequest();
        
        request.set(StringRequestParameterKey.COMMAND, Commands.RESOLVE_ATTRIBUTES_FROM_DIRECTORY);
        request.set(StringRequestParameterKey.BASE_DIR, baseDir.getAbsolutePath());

        return callServer(request);
        
    }

    private Response callServer(Request r) throws AspClientException {
        Command command = r.getCommand();
        if (command==null) {
            throw new AspClientException("No command set");
        }
        
        try (Socket aspSocket = new Socket(InetAddress.getLoopbackAddress(), portNumber);
                PrintWriter out = new PrintWriter(aspSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(aspSocket.getInputStream()));) {
            
            String requestString = r.convertToString();
            LOG.debug("sending:\n{}",requestString);
            out.println(requestString);
            out.println(Request.TERMINATOR);
            
            String fromServer = null;
            StringBuilder result = new StringBuilder();
            while ((fromServer = in.readLine()) != null) {
                LOG.debug("receiving:{}",fromServer);
                if (fromServer.equals(Response.TERMINATOR)) {
                    break;
                }
                result.append(fromServer);
                result.append('\n');
            }
            
            Response response = Response.convertFromString(result.toString());
            if (response.failed()) {
                throw new AspClientException("Failed:"+response.getErrorMessage());
            }
            return response;
            
        } catch (Exception e) {
            throw new AspClientException("Was not able to convert local file", e);
        }
    }

}
