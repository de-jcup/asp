package de.jcup.asp.integrationtest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.BindException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jcup.asp.api.Response;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.CryptoAccess;
import de.jcup.asp.core.LaunchException;
import de.jcup.asp.server.asciidoctorj.EmbeddedAsciidoctorJServerLauncher;

public class EmbeddedServerIntTest {

    private int port;
    private EmbeddedAsciidoctorJServerLauncher serverToTest;

    @Before
    public void before() {
        port = 4447;
        serverToTest = new EmbeddedAsciidoctorJServerLauncher();
    }
    
    @After
    public void after() {
        serverToTest.stopServer();
    }


    @Test
    public void server_launch_by_embeded_server() throws Exception {
        String key = serverToTest.launch(port);
        callServerAliveMultipleTimes(key, port);

    }

    @Test
    public void server_launch_by_embeded_server_but_client_uses_wrong_key() throws Exception {

        String key = serverToTest.launch(port);
        CryptoAccess a = new CryptoAccess();
        String keyFromOtherServer = a.getSecretKey();
        assertNotEquals(key, keyFromOtherServer);

        callServerAliveMultipleTimes(keyFromOtherServer, port, false,1);

    }

    @Test
    public void server_launch_by_embeded_server_duplicated() throws Exception {

        EmbeddedAsciidoctorJServerLauncher embedded2 = new EmbeddedAsciidoctorJServerLauncher();
        serverToTest.launch(port);
        try {
            embedded2.launch(port);
            fail("no exception thrown");
        } catch (LaunchException e) {
            assertTrue(e.getCause() instanceof BindException);
        }

    }

    @Test
    public void convert_file_to_html_works() throws Exception {
        /* prepare */
        AspClient client = launchServerAndGetPreparedClient();

        Path adocfile = createSimpleAdocTestFile();
        HashMap<String, Object> options = new HashMap<String, Object>();
        
        /* execute */
        Response response = client.convertFile(adocfile, options);
        
        /* test */
        Path result = response.getResultFilePath();
        System.out.println("result:" + result);
        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().getName().endsWith(".html"));

    }

    private Path createSimpleAdocTestFile() throws IOException {
        Path adocfile = Files.createTempFile("asp_test", ".adoc");
        Files.write(adocfile, "== Test\nThis is just a test".getBytes());
        return adocfile;
    }

    @Test
    public void convert_file_to_pdf_works() throws Exception {
        /* prepare */
        AspClient client = launchServerAndGetPreparedClient();

        Path adocfile = createSimpleAdocTestFile();
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("backend", "pdf");
        
        /* execute */
        Response response = client.convertFile(adocfile, options);
        
        /* test */
        Path result = response.getResultFilePath();
        System.out.println("result:" + result);
        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().getName().endsWith(".pdf"));

    }

    private AspClient launchServerAndGetPreparedClient() throws LaunchException {
        String key = serverToTest.launch(port);
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        return client;
    }

    private void callServerAliveMultipleTimes(String key, int port) {
        callServerAliveMultipleTimes(key, port, true,1000);
    }

    private void callServerAliveMultipleTimes(String key, int port, boolean expected, int amount) {
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        for (int i = 0; i < amount; i++) {
            boolean alive = client.isServerAlive();
            assertEquals(expected, alive);
        }
    }

}