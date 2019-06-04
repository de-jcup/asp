package de.jcup.asp.server.core;

import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;

public interface ClientRequestHandler {

    Response handleRequest(Request request);

}
