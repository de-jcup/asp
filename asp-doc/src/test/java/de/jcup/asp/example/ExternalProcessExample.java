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
package de.jcup.asp.example;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import de.jcup.asp.api.Response;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.server.asciidoctorj.launcher.ExternalProcessAsciidoctorJServerLauncher;

public class ExternalProcessExample {


    public static void main(String[] args) throws Exception {
        Path adocfile = createExampleAsciidocFile();
        String pathToServerJar = ensurePathToServerDistJar();
        
        // tag::launcherExample[]
        ExternalProcessAsciidoctorJServerLauncher launcher = new ExternalProcessAsciidoctorJServerLauncher(pathToServerJar, 4449); // <1>
        try {
            /* launch server*/
            String serverSecret = launcher.launch(30); // <2>
            
            /* create client with secret from server for encrypted communication */
            AspClient client = new AspClient(serverSecret); // <3>
            client.setPortNumber(4449);
            
            /* now convert Asciidoc to HTML by ASP client */
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("backend", "html");// <4>
            
            /* get the result and show it inside browser:*/
            Response response = client.convertFile(adocfile, options, null);// <5>
            Path resultFile = response.getResultFilePath();
            
            Desktop.getDesktop().open(resultFile.toFile());
        }finally {
            launcher.stopServer(); //<6>
        }
        // end::launcherExample[]
        
        
    }

     static Path createExampleAsciidocFile() throws IOException {
        Path adocfile = Files.createTempFile("asp_test", ".adoc");
        Files.write(adocfile, (":toc:\n== Headline1\nSome text...\n\n== Headline2\nThis is just an example content for ASP...").getBytes());
        return adocfile;
    }

    private static String ensurePathToServerDistJar() {
        String pathToServerJar= System.getProperty("user.home")+ "/.m2/repository/de/jcup/asp/asp-server-asciidoctorj/0.3.0/asp-server-asciidoctorj-0.3.0-dist.jar";
        if (! new File(pathToServerJar).exists()) {
            throw new RuntimeException("Distribution jar missing - please download server distribution into your local maven repository");
        }
        return pathToServerJar;
    }
}
