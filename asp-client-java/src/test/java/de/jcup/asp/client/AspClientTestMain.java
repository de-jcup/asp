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
        
        Response response = new AspClient(serverSecretkey).convertFile(adocfile, options);
        Path path = response.getResultFilePath();
        
        System.out.println("got: result path:"+path);
        
        ServerLog serverLog = response.getServerLog();
        for (ServerLogEntry entry: serverLog.getEntries()) {
            System.out.println("Logs:"+entry);
        }
        
//        Desktop.getDesktop().open(path.toFile());
    }
}
