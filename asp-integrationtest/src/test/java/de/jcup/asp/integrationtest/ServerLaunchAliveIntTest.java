package de.jcup.asp.integrationtest;

import static org.junit.Assert.*;

import org.junit.Test;

import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.server.asciidoctorj.EmbeddedAsciidoctorJServerLauncher;
import de.jcup.asp.server.asciidoctorj.launcher.AsciidoctorJServerLauncher;

public class ServerLaunchAliveIntTest {

    private static final int SERVER_PORT = 4447;

    @Test
    public void server_laucnh_by_jar() throws Exception {
        /* prepare */
        // FIXME: ensure buildDist done and available... also path not correct in
        // gradle.
        AsciidoctorJServerLauncher launcher = new AsciidoctorJServerLauncher("./../asp-server-asciidoctorj/build/libs/asp-server-asciidoctorj-0.3.0.jar", SERVER_PORT);
        try {

            launcher.setOutputHandler(new OutputHandler() {

                @Override
                public void output(String message) {
                    System.out.println(message);

                }
            });
            String key = launcher.launch(30);
            callServerAliveMultipleTimes(key);
        } finally {
            launcher.stopServer();

        }

    }
    @Test
    public void server_launch_by_embeded_server() throws Exception {
        try {
            EmbeddedAsciidoctorJServerLauncher embedded= new EmbeddedAsciidoctorJServerLauncher();
            String key = embedded.launch(4447);
            
            callServerAliveMultipleTimes(key);
        } finally {

        }

    }

    private void callServerAliveMultipleTimes(String key) {
        System.out.println("Key fetched:" + key);
        AspClient client = new AspClient(key);
        client.setPortNumber(4447);
        for (int i = 0; i < 1000; i++) {
            boolean alive = client.isServerAlive();
            System.out.println("alive=" + alive);
            assertTrue(alive);
        }
    }

}
