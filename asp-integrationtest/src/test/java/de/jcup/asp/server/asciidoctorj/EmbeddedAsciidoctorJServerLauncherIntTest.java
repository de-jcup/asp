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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.BindException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.jcup.asp.api.Response;
import de.jcup.asp.api.asciidoc.Attributes;
import de.jcup.asp.api.asciidoc.Options;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.CryptoAccess;
import de.jcup.asp.core.LaunchException;
import de.jcup.asp.integrationtest.TestConstants;

public class EmbeddedAsciidoctorJServerLauncherIntTest {

    private int port;
    private EmbeddedAsciidoctorJServerLauncher launcherToTest;

    @Rule
    public Timeout timeOutRule = Timeout.seconds(2220);
    
    @Before
    public void before() {
        port = TestConstants.EMBEDDED_TESTSERVER_PORT+1;
        launcherToTest = new EmbeddedAsciidoctorJServerLauncher();
    }

    @After
    public void after() {
        launcherToTest.stopServer();
    }

    @Test
    public void server_launch_by_embeded_server() throws Exception {
        String key = launcherToTest.launch(port);
        callServerAliveMultipleTimes(key, port);

    }

    @Test
    public void server_launch_by_embeded_server_but_client_uses_wrong_key() throws Exception {

        String key = launcherToTest.launch(port);
        CryptoAccess a = new CryptoAccess();
        String keyFromOtherServer = a.getSecretKey();
        assertNotEquals(key, keyFromOtherServer);

        callServerAliveMultipleTimes(keyFromOtherServer, port, false, 1);

    }

    @Test
    public void server_launch_by_embeded_server_duplicated() throws Exception {

        EmbeddedAsciidoctorJServerLauncher embedded2 = new EmbeddedAsciidoctorJServerLauncher();
        launcherToTest.launch(port);
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

        Path adocfile = createSimpleAdocTestFile("To check html works...");
        Options options = Options.builder().backend("html").build();
        Attributes attributes = Attributes.builder().build();

        /* execute */
        Response response = client.convertFile(adocfile, options, attributes, null);

        /* test */
        assertFalse(response.failed());
        Path result = response.getResultFilePath();
        System.out.println("result:" + result);
        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().getName().endsWith(".html"));

    }

    @Test
    public void convert_file_to_pdf_works() throws Exception {
        /* prepare */
        AspClient client = launchServerAndGetPreparedClient();

        Path adocfile = createSimpleAdocTestFile("To check pdf works...");
        
        Options options = Options.builder().backend("pdf").build();
        Attributes attributes = Attributes.builder().build();

        /* execute */
        Response response = client.convertFile(adocfile, options, attributes, null);

        /* test */
        assertFalse(response.failed());
        Path result = response.getResultFilePath();
        System.out.println("result:" + result);
        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().getName().endsWith(".pdf"));

    }
    
    private Path createSimpleAdocTestFile(String addition) throws IOException {
        Path adocfile = Files.createTempFile("asp_test", ".adoc");
        Files.write(adocfile, ("== Test\nThis is just a test\n"+addition).getBytes());
        return adocfile;
    }

    private AspClient launchServerAndGetPreparedClient() throws LaunchException {
        String key = launcherToTest.launch(port);
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        return client;
    }

    private void callServerAliveMultipleTimes(String key, int port) {
        callServerAliveMultipleTimes(key, port, true, 1000);
    }

    private void callServerAliveMultipleTimes(String key, int port, boolean expected, int amount) {
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        for (int i = 0; i < amount; i++) {
            boolean alive = client.isServerAlive(null);
            assertEquals(expected, alive);
        }
    }

}
