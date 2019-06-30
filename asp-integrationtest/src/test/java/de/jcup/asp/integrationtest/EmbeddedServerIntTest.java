package de.jcup.asp.integrationtest;

import static org.junit.Assert.*;

import java.net.BindException;

import org.junit.Before;
import org.junit.Test;

import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.CryptoAccess;
import de.jcup.asp.core.LaunchException;
import de.jcup.asp.server.asciidoctorj.EmbeddedAsciidoctorJServerLauncher;

public class EmbeddedServerIntTest {

    private int port;

    @Before
    public void before() {
        port = 4447;
    }

    @Test
    public void server_launch_by_embeded_server() throws Exception {

        EmbeddedAsciidoctorJServerLauncher embedded = new EmbeddedAsciidoctorJServerLauncher();
        try {
            String key = embedded.launch(port);
            callServerAliveMultipleTimes(key, port);
        } finally {
            embedded.stopServer();
        }

    }

    @Test
    public void server_launch_by_embeded_server_but_client_uses_wrong_key() throws Exception {

        EmbeddedAsciidoctorJServerLauncher embedded = new EmbeddedAsciidoctorJServerLauncher();
        try {
            String key = embedded.launch(port);
            CryptoAccess a = new CryptoAccess();
            String keyFromOtherServer = a.getSecretKey();
            assertNotEquals(key, keyFromOtherServer);

            callServerAliveMultipleTimes(keyFromOtherServer, port, false);
        } finally {
            embedded.stopServer();
        }

    }

    @Test
    public void server_launch_by_embeded_server_duplicated() throws Exception {

        EmbeddedAsciidoctorJServerLauncher embedded = new EmbeddedAsciidoctorJServerLauncher();
        EmbeddedAsciidoctorJServerLauncher embedded2 = new EmbeddedAsciidoctorJServerLauncher();
        try {
            embedded.launch(port);
            try {
                embedded2.launch(port);
                fail("no exception thrown");
            } catch (LaunchException e) {
                assertTrue(e.getCause() instanceof BindException);
            }
        } finally {
            embedded.stopServer();
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
