package de.jcup.asp.integrationtest;

import static org.junit.Assert.*;

import java.net.BindException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.CryptoAccess;
import de.jcup.asp.core.LaunchException;
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.server.asciidoctorj.EmbeddedAsciidoctorJServerLauncher;
import de.jcup.asp.server.asciidoctorj.launcher.ExternalProcessAsciidoctorJServerLauncher;

public class ServerLaunchAliveIntTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static final int SERVER_PORT = 4447;
    
    @Test
    public void server_launch_by_jar() throws Exception {
        /* prepare */
        // FIXME: ensure buildDist done and available... also path not correct in
        // gradle.
        int port = SERVER_PORT;
        ExternalProcessAsciidoctorJServerLauncher launcher = new ExternalProcessAsciidoctorJServerLauncher("./../asp-server-asciidoctorj/build/libs/asp-server-asciidoctorj-0.3.0.jar", port);
        try {

            launcher.setOutputHandler(new OutputHandler() {

                @Override
                public void output(String message) {
                    System.out.println(message);

                }
            });
            String key = launcher.launch(30);
            callServerAliveMultipleTimes(key,port);
        } finally {
            launcher.stopServer();
        }
    }

    @Test
    public void server_launch_by_embeded_server() throws Exception {
        int port = SERVER_PORT;
        EmbeddedAsciidoctorJServerLauncher embedded = new EmbeddedAsciidoctorJServerLauncher();
        try{
            String key = embedded.launch(port);
            callServerAliveMultipleTimes(key, port);
        }finally {
            embedded.stopServer();
        }

    }
    
    @Test
    public void server_launch_by_embeded_server_but_client_uses_wrong_key() throws Exception {
        int port = SERVER_PORT;
        EmbeddedAsciidoctorJServerLauncher embedded = new EmbeddedAsciidoctorJServerLauncher();
        try{
            String key = embedded.launch(port);
            CryptoAccess a = new CryptoAccess();
            String keyFromOtherServer = a.getSecretKey();
            assertNotEquals(key, keyFromOtherServer);
            
            callServerAliveMultipleTimes(keyFromOtherServer, port);
        }finally {
            embedded.stopServer();
        }

    }
    
    @Test
    public void server_launch_by_embeded_server_duplicated() throws Exception {
        int port = SERVER_PORT;
        EmbeddedAsciidoctorJServerLauncher embedded = new EmbeddedAsciidoctorJServerLauncher();
        EmbeddedAsciidoctorJServerLauncher embedded2 = new EmbeddedAsciidoctorJServerLauncher();
        try{
            embedded.launch(port);
            try {
                embedded2.launch(port);
                fail("no exception thrown");
            }catch(LaunchException e) {
                assertTrue(e.getCause() instanceof BindException);
            }
        }finally {
            embedded.stopServer();
        }

    }
    

    private void callServerAliveMultipleTimes(String key,int port) {
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        for (int i = 0; i < 1000; i++) {
            boolean alive = client.isServerAlive();
            assertTrue(alive);
        }
    }

}
