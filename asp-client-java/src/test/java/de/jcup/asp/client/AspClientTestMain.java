package de.jcup.asp.client;

import java.awt.Desktop;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AspClientTestMain {

    public static void main(String[] args) throws Exception{
        
        Path adocfile = Files.createTempFile("test", ".adoc");
        String dateStr = SimpleDateFormat.getDateTimeInstance().format(new Date());
        Files.newBufferedWriter(adocfile).append("== Interests in asciidoc\nsomething important...\n\nTIP: Do not write word, but asciidoc ;-)\n\nNOTE: its now:"+dateStr).close();
        
        Map<String, Object> options = new HashMap<>();
        options.put("backend","pdf");
        Path result = new AspClient().convertFile(adocfile, options);
        System.out.println("got: result:"+result);
        
        Desktop.getDesktop().open(result.toFile());
    }
}
