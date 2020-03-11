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
package de.jcup.asp.server.asciidoctorj.launcher;

import static de.jcup.asp.integrationtest.AdocTestFiles.*;
import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Response;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.client.DefaultAspClientProgressMonitor;
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.integrationtest.FullIntegrationTestRule;
import de.jcup.asp.integrationtest.TestConstants;

public class ExternalProcessAsciidoctorJServerLauncherIntTest {

    private int port;

    private static final Logger LOG = LoggerFactory.getLogger(ExternalProcessAsciidoctorJServerLauncherIntTest.class);

    @Rule
    public FullIntegrationTestRule fullIntegrationTestRule = new FullIntegrationTestRule();

    private ExternalProcessAsciidoctorJServerLauncher launcherToTest;

    @Before
    public void before() {
        port = TestConstants.EXTERNAL_PROCESS_PORT;
        launcherToTest = new ExternalProcessAsciidoctorJServerLauncher(fullIntegrationTestRule.getEnsuredPathToServerJar(), port);
        launcherToTest.setPathToJavaBinary(fullIntegrationTestRule.getPathToJavaBinaryOrNull());

        launcherToTest.setOutputHandler(new OutputHandler() {

            @Override
            public void output(String message) {
                System.out.println(message);

            }
        });
    }

    @After
    public void after() {
        if (launcherToTest != null) {
            launcherToTest.stopServer();
        }
    }

    @Test
    public void server_launch_by_jar() throws Exception {

        String key = launcherToTest.launch(30);
        callServerAliveMultipleTimes(key, port);
    }

    @Test
    public void long_running_action_like_convert_file_to_pdf_can_be_canceled() throws Exception {
        /* prepare */
        String key = launcherToTest.launch(30);
        AspClient client = new AspClient(key);
        client.setPortNumber(port);

        Path adocfile = createSimpleAdocTestFile();
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("backend", "pdf");

        DefaultAspClientProgressMonitor monitor = new DefaultAspClientProgressMonitor();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    monitor.setCanceled(true);
                    LOG.info(">> canceld by user!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        Thread delayedCancelByUserThread = new Thread(runnable, "Simulated (delayed) cancel by user");
        delayedCancelByUserThread.start();

        /* execute */
        LOG.info("> start convert");
        Response response = client.convertFile(adocfile, options, monitor);
        LOG.info("> end of convert call");

        /* test */
        assertTrue(response.failed());
        assertTrue(response.getErrorMessage().contains("canceled"));

    }

    private void callServerAliveMultipleTimes(String key, int port) {
        callServerAliveMultipleTimes(key, port, true);
    }

    private void callServerAliveMultipleTimes(String key, int port, boolean expected) {
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        for (int i = 0; i < 1000; i++) {
            boolean alive = client.isServerAlive(null);
            assertEquals(expected, alive);
        }
    }

}
