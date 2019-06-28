package de.jcup.asp.server.asciidoctorj;

import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;
import de.jcup.asp.server.core.CoreAspServer;

public class AsciidoctorJServer {

    CoreAspServer coreAspServer;

    public AsciidoctorJServer() {
        coreAspServer = new CoreAspServer();
    }

    public void start(int port) {
        if (port > 0) {
            coreAspServer.setPortNumber(port);
        }
        AsciidoctorService.INSTANCE.warmUp();

        coreAspServer.setRequestHandler(new AsciiDoctorJServerClientRequestHandler());

        coreAspServer.start();

    }

}
