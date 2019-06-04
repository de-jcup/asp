package de.jcup.asp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.ResponseResultKeys;
import de.jcup.asp.server.core.ClientRequestHandler;

public class DummyOutputClientRequestHandler implements ClientRequestHandler{


private static final Logger LOG = LoggerFactory.getLogger(DummyOutputClientRequestHandler.class);

    @Override
    public Response handleRequest(Request request) {
        LOG.info("Request:\n"+request.convertToString());
        Command command = request.getCommand();
        Response response= new Response();
        if (Commands.CONVERT_LOCALFILE.equals(command)) {
            response.set(ResponseResultKeys.RESULT_FILEPATH, "~/theresult");
        }else {
            response.setErrorMessage("Unsupported command:"+command);
        }
        return response;
    }

}
