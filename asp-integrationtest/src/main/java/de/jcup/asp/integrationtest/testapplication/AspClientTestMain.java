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
package de.jcup.asp.integrationtest.testapplication;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.jcup.asp.api.Response;
import de.jcup.asp.api.ServerLog;
import de.jcup.asp.api.ServerLogEntry;
import de.jcup.asp.api.asciidoc.AsciidocAttributes;
import de.jcup.asp.api.asciidoc.AsciidocOptions;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.client.AspClientException;
import de.jcup.asp.core.LaunchException;
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.server.asciidoctorj.launcher.ExternalProcessAsciidoctorJServerLauncher;

/**
 * Usage:
 * <ol>
 * <li>start a ASP server by AspServerTestMain and fetch the secret key from
 * console output.</li>
 * <li>start this client and use the formerly fetched secret key as first
 * argument (optional: if you want to render an explicit `.adoc` file and not
 * the generic example then define the path as second argument)</li>
 * 
 * @author de-jcup
 *
 */
public class AspClientTestMain {

    private class StartData {
        String serverSecretkey;
        ExternalProcessAsciidoctorJServerLauncher launcher;
        int portNumber = 4444;
        String exampleFileLocation;
        Path adocfile;
    }

    public static void main(String[] args) throws Exception {
        new AspClientTestMain().start(args);
    }

    private void start(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: " + AspClientTestMain.class.getName() + " $serverSecret|embedded [$exampleFileLocation]");
        }
        StartData data = new StartData();
        initDataWithParameters(args, data);
        System.out.println("Will start asciidoc rendering for file:" + data.adocfile);

        startASPserverWhenEmbeddedVariant(data);
        AspClient client = initClient(data);
        
        /* @formatter:off */
        Response response = convertAsciidocFileByASP(data, client);
        System.out.println(">> reponse received");

        String errorMessage = response.getErrorMessage();
        if (errorMessage != null && !errorMessage.isEmpty()) {
            System.out.println("Error message recevied: " + errorMessage);
        }

        ServerLog serverLog = response.getServerLog();
        for (ServerLogEntry entry : serverLog.getEntries()) {
            System.out.println("Logs:" + entry);
        }

        showInfoAboutResult(data, response);
    }

    private Response convertAsciidocFileByASP(StartData data, AspClient client) throws AspClientException {
        AsciidocOptions asciidocOptions = AsciidocOptions.builder().
                backend("pdf").
                build();

        AsciidocAttributes asciidocAttributes = AsciidocAttributes.builder().
                sourceHighlighter("coderay").
                customAttribute("coderay-css", "style").
                build();
        /* @formatter:on */
        Response response = client.convertFile(data.adocfile, asciidocOptions, asciidocAttributes, null);
        return response;
    }

    private void showInfoAboutResult(StartData data, Response response) {
        Path path = response.getResultFilePath();
        File file = path.toFile();
        if (data.launcher != null) {
            data.launcher.stopServer();
        }
        if (file.getName().endsWith("theresult")) {
            System.out.println("Fake request handler active will not open output.");
        }else {
            System.out.println("Output available at:" + path);
        }
    }

    private AspClient initClient(StartData data) {
        AspClient client = new AspClient(data.serverSecretkey);
        client.setPortNumber(data.portNumber);
        client.setOutputHandler(new OutputHandler() {

            @Override
            public void output(String message) {
                System.out.println("message:" + message);
            }
        });
        client.setShowCommunication(true);
        return client;
    }

    private void startASPserverWhenEmbeddedVariant(StartData data) throws LaunchException {
        boolean startServerEmbedded = "embedded".equalsIgnoreCase(data.serverSecretkey);

        if (startServerEmbedded) {
            File distLibsFolder = new File("./../asp-server-asciidoctorj/build/libs");
            if (!distLibsFolder.exists()) {
                throw new IllegalStateException("Libs folder does not exist:" + distLibsFolder.getAbsolutePath());
            }
            File[] distJarFiles = distLibsFolder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    String name = file.getName();
                    if (!name.endsWith(".jar")) {
                        return false;
                    }
                    if (!name.contains("-dist")) {
                        return false;
                    }
                    return true;
                }
            });
            if (distJarFiles.length != 1) {
                throw new IllegalStateException("Did expect 1 distribution jar, but found:" + distJarFiles.length + 
                        " dist jars inside "+distLibsFolder.getAbsolutePath());
            }
            String absolutePath = distJarFiles[0].getAbsolutePath();
            System.out.println(">> Embedded: Launching external process asp server: " + absolutePath);
            data.launcher = new ExternalProcessAsciidoctorJServerLauncher(absolutePath, data.portNumber);
            data.serverSecretkey = data.launcher.launch(10);

        }
    }

    private void initDataWithParameters(String[] args, StartData data) throws IOException {
        data.serverSecretkey = args[0];
        if (args.length > 1) {
            data.exampleFileLocation = args[1];
        }

        if (data.exampleFileLocation == null) {
            data.adocfile = Files.createTempFile("test", ".adoc");
            String dateStr = SimpleDateFormat.getDateTimeInstance().format(new Date());
            Files.newBufferedWriter(data.adocfile)
                    .append("== Interests in asciidoc\nsomething important...\n\ninclude::not-existing.adoc[]\n\nTIP: Do not write word, but asciidoc ;-)\n\nNOTE: its now:" + dateStr).close();
        } else {
            data.adocfile = Paths.get(data.exampleFileLocation);
        }
    }
}
