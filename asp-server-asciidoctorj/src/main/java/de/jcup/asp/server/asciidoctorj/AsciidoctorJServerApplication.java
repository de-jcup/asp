package de.jcup.asp.server.asciidoctorj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;
import de.jcup.asp.server.core.CoreAspServer;

public class AsciidoctorJServerApplication {

    private static final String SYSTEM_PROPERTY__ASP_SERVER_PORT = "asp.server.port";
    private static final Logger LOG = LoggerFactory.getLogger(AsciidoctorJServerApplication.class);

    public static void main(final String[] args) {
        String portProperty = System.getProperty(SYSTEM_PROPERTY__ASP_SERVER_PORT);
        int port=-1;
        try {
            if (portProperty != null) {
                port=Integer.parseInt(portProperty);
            }
        } catch (NumberFormatException e) {
            LOG.error("worng port definition:{} using default", portProperty);
        }    
        new AsciidoctorJServer().start(port);
    }

}
