package de.jcup.asp.integrationtest;

import java.util.Base64;

import org.junit.Test;

import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.server.asciidoctorj.launcher.ServerLauncher;

public class ServerLaunchAliveIntTest {

    @Test
    public void test() throws Exception{
        /* prepare */
        // FIXME: ensure buildDist done and available... also path not correct in gradle.
        ServerLauncher launcher = new ServerLauncher("./../asp-server-asciidoctorj/build/libs/asp-server-asciidoctorj-0.3.0.jar",4447);
        launcher.setOutputHandler(new OutputHandler() {
            
            @Override
            public void output(String message) {
                System.out.println(message);
                
            }
        });
       
        String key = launcher.launch();
        System.out.println("Key fetched:"+key);
        AspClient client = new AspClient(key);
        client.setPortNumber(4447);
        boolean alive= client.isServerAlive();
        System.out.println("alive="+alive);
        
    }

}
