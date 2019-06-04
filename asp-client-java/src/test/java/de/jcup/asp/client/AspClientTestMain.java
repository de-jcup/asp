package de.jcup.asp.client;

import java.awt.Desktop;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.jcup.asp.api.Backend;

public class AspClientTestMain {

    public static void main(String[] args) throws Exception{
        
        Path adocfile = Files.createTempFile("test", ".adoc");
        String dateStr = SimpleDateFormat.getDateTimeInstance().format(new Date());
        Files.newBufferedWriter(adocfile).append("== Interests in asciidoc\nsomething important...\n\nTIP: Do not write word, but asciidoc ;-)\n\nNOTE: its now:"+dateStr).close();
        
        Path result = new AspClient().convertLocal(adocfile, Backend.PDF);
        System.out.println("got: result:"+result);
        
        Desktop.getDesktop().open(result.toFile());
    }
}
