package de.jcup.asp.server.asciidoctorj;

import java.io.IOException;

import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;
import de.jcup.asp.server.core.ClientRequestHandler;
import de.jcup.asp.server.core.CoreAspServer;

public class AsciidoctorJServer {

    CoreAspServer coreAspServer;

    public AsciidoctorJServer() {
        coreAspServer = new CoreAspServer();
    }

    public void start(int port) throws IOException {
        if (port > 0) {
            coreAspServer.setPortNumber(port);
        }
        warmup();

        coreAspServer.setRequestHandler(createRequesthandler());

        coreAspServer.start();

    }

    protected void warmup() {
        AsciidoctorService.INSTANCE.warmUp();
    }

    protected ClientRequestHandler createRequesthandler() {
        return new AsciiDoctorJServerClientRequestHandler();
    }
    

}
