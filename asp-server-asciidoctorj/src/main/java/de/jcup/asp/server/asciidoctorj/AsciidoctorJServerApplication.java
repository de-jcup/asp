package de.jcup.asp.server.asciidoctorj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;
import de.jcup.asp.server.core.AspServer;

public class AsciidoctorJServerApplication {

    private static final String SYSTEM_PROPERTY__ASP_SERVER_PORT = "asp.server.port";
    private static final Logger LOG = LoggerFactory.getLogger(AsciidoctorJServerApplication.class);

    public static void main(final String[] args) {

        AsciidoctorService.INSTANCE.warmUp();
        
        AspServer server = new AspServer();
        
        server.setRequestHandler(new AsciiDoctorJServerClientRequestHandler());
        String portProperty = System.getProperty(SYSTEM_PROPERTY__ASP_SERVER_PORT);
        try {
            if (portProperty != null) {
                server.setPortNumber(Integer.parseInt(portProperty));
            }
        } catch (NumberFormatException e) {
            LOG.error("worng port definition:{} using default", portProperty);
        }
        server.start();
    }

}
