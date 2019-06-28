package de.jcup.asp.server.asciidoctorj;

import java.util.concurrent.TimeUnit;

import de.jcup.asp.core.ASPLauncher;

public class EmbeddedAsciidoctorJServerLauncher implements ASPLauncher{

    
    private AsciidoctorJServer server;

    public EmbeddedAsciidoctorJServerLauncher() {
        server = new AsciidoctorJServer();
        server.coreAspServer.setOnExitKeepAlive(true);
    }
   
    /**
     * Starts embedded server 
     * @param port
     * @return secret key
     */
    public String launch(int port) {
        Runnable r = new Runnable() {
            
            @Override
            public void run() {
                server.start(port);
            }
        };
        Thread t = new Thread(r, "Embedded ASP Server");
        t.start();
        
        String key =  waitFortSecretKey();
        while (!server.coreAspServer.isReady()) {
            if (server.coreAspServer.hasFailed()) {
                
            }
            wait500Millis();
        }
        return key;
    }

    private String waitFortSecretKey() {
        int maxLoops = 10;
        int loop=0;
        while (server==null) {
            loop++;
            if (loop>maxLoops) {
                throw new IllegalStateException("Server failure-no secret created");
            }
            wait500Millis();
        }
        return server.coreAspServer.getCryptoAccess().getSecretKey();
    }

    private void wait500Millis() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
