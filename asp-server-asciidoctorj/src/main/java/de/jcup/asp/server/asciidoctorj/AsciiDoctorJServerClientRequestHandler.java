package de.jcup.asp.server.asciidoctorj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringResponseResultKey;
import de.jcup.asp.server.asciidoctorj.service.ConvertLocalFileService;
import de.jcup.asp.server.asciidoctorj.service.ConvertLocalFileServiceImpl;
import de.jcup.asp.server.asciidoctorj.service.ResolveAttributesService;
import de.jcup.asp.server.core.ClientRequestHandler;

public class AsciiDoctorJServerClientRequestHandler implements ClientRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AsciiDoctorJServerClientRequestHandler.class);
    private ConvertLocalFileService converterLocalFileService;
    private ResolveAttributesService resolveAttributesService;

    public AsciiDoctorJServerClientRequestHandler() {
        this.converterLocalFileService = createConverterLocalFileService();
        this.resolveAttributesService = createResolveAttributeService();
    }
    
    @Override
    public Response handleRequest(Request request) {
        LOG.debug("Request:\n" + request.convertToString());
        Command command = request.getCommand();
        Response response = new Response();
        response.set(StringResponseResultKey.VERSION, Version.getVersion()); 

        if (Commands.CONVERT_FILE.equals(command)) {
            converterLocalFileService.convertFile(request, response);
        } else if (Commands.RESOLVE_ATTRIBUTES_FROM_DIRECTORY.equals(command)) {
            resolveAttributesService.resolveAttributesFromDirectory(request,response);
        } else {
            response.setErrorMessage("Unsupported command:" + command);
        }
        return response;

    }
    
    ResolveAttributesService createResolveAttributeService() {
        return ResolveAttributesService.INSTANCE;
    }

    ConvertLocalFileService createConverterLocalFileService() {
        return ConvertLocalFileServiceImpl.INSTANCE;
    }

}
