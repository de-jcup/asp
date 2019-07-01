package de.jcup.asp.integrationtest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.server.asciidoctorj.launcher.ExternalProcessAsciidoctorJServerLauncher;

public class ExternalServerIntTest {

    private int port;
    

    @Rule
    public FullIntegrationTestRule fullIntegrationTestRule = new FullIntegrationTestRule();
    
    @Before
    public void before() {
        port = 4447;
    }

    @Test
    public void server_launch_by_jar() throws Exception {
        ExternalProcessAsciidoctorJServerLauncher launcher = new ExternalProcessAsciidoctorJServerLauncher(fullIntegrationTestRule.getEnsuredPathToServerJar(), port);
        try {

            launcher.setOutputHandler(new OutputHandler() {

                @Override
                public void output(String message) {
                    System.out.println(message);

                }
            });
            String key = launcher.launch(30);
            callServerAliveMultipleTimes(key, port);
        } finally {
            launcher.stopServer();
        }
    }

    private void callServerAliveMultipleTimes(String key, int port) {
        callServerAliveMultipleTimes(key, port, true);
    }

    private void callServerAliveMultipleTimes(String key, int port, boolean expected) {
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        for (int i = 0; i < 1000; i++) {
            boolean alive = client.isServerAlive();
            assertEquals(expected, alive);
        }
    }

}
