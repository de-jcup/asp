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
package de.jcup.asp.client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jcup.asp.api.Response;
import de.jcup.asp.api.ServerLog;
import de.jcup.asp.api.ServerLogEntry;

public class AspClientTestMain {

    public static void main(String[] args) throws Exception{
        String serverSecretkey = args[0];
        Path adocfile = Files.createTempFile("test", ".adoc");
        String dateStr = SimpleDateFormat.getDateTimeInstance().format(new Date());
        Files.newBufferedWriter(adocfile).append("== Interests in asciidoc\nsomething important...\n\ninclude::not-existing.adoc[]\n\nTIP: Do not write word, but asciidoc ;-)\n\nNOTE: its now:"+dateStr).close();
        
        Map<String, Object> options = new HashMap<>();
        options.put("backend","pdf");
        
        Response response = new AspClient(serverSecretkey).convertFile(adocfile, options,null);
        Path path = response.getResultFilePath();
        
        System.out.println("got: result path:"+path);
        
        ServerLog serverLog = response.getServerLog();
        for (ServerLogEntry entry: serverLog.getEntries()) {
            System.out.println("Logs:"+entry);
        }
        
//        Desktop.getDesktop().open(path.toFile());
    }
}
