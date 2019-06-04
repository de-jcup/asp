package de.jcup.asp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Backend;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Constants;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.RequestParameterKeys;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.ResponseResultKeys;

public class AspClient {

    private static final Logger LOG = LoggerFactory.getLogger(AspClient.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public Path convertLocal(Path adocfile, Backend backend) throws AspClientException {
        Request request = new Request();
        request.set(RequestParameterKeys.COMMAND, Commands.CONVERT_LOCALFILE);
        request.set(RequestParameterKeys.BACKEND, backend.convertToString());
        request.set(RequestParameterKeys.SOURCE_FILEPATH, adocfile.toAbsolutePath().toString());

        Response response = callServer(request);
        if (response.failed()) {
            throw new AspClientException("Failed:"+response.getErrorMessage());
        }
        Path path = Paths.get(response.get(ResponseResultKeys.RESULT_FILEPATH));
        return path;
    }

    private Response callServer(Request r) throws AspClientException {
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
            
            return Response.convertFromString(result.toString());
            
        } catch (Exception e) {
            throw new AspClientException("Was not able to convert local file", e);
        }
    }

}
