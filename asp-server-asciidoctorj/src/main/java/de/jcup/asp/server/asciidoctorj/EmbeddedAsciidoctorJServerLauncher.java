/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.asp.server.asciidoctorj;

import de.jcup.asp.core.ASPLauncher;
import de.jcup.asp.core.LaunchException;

public class EmbeddedAsciidoctorJServerLauncher implements ASPLauncher {

    private AsciidoctorJServer server;

    public EmbeddedAsciidoctorJServerLauncher() {
        server = createServer();
    }

    protected AsciidoctorJServer createServer() {
        return new AsciidoctorJServer();
    }

    public void stopServer() {
        server.coreAspServer.stop();
    }

    /**
     * Starts embedded server
     * 
     * @param port
     * @return secret key
     */
    public String launch(int port) throws LaunchException{
        EmbeddedServerRunnable r = new EmbeddedServerRunnable(port);
        Thread t = new Thread(r, "Embedded ASP Server");
        t.start();

        /* fetch key*/
        String key = waitFortSecretKey();
        /* wait for server being read for client usage*/
        while (r.failure==null && !server.coreAspServer.isReady()) {
            wait500Millis();
        }
        if (r.failure!=null) {
            throw new LaunchException("Was not able to launch ASP server",r.failure);
        }
        return key;
    }

    private String waitFortSecretKey() {
        int maxLoops = 10;
        int loop = 0;
        while (server == null) {
            loop++;
            if (loop > maxLoops) {
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

    private class EmbeddedServerRunnable implements Runnable{
    
        private Exception failure;
        private int port;
        
        private EmbeddedServerRunnable(int port) {
            this.port=port;
        }
        
        @Override
        public void run() {
            try {
                server.start(port);
            }catch(Exception e) {
                this.failure=e;
            }
        }
        
    }

}
