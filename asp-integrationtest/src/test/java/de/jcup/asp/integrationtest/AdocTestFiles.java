package de.jcup.asp.integrationtest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AdocTestFiles {
    
    public static Path createSimpleAdocTestFile() throws IOException {
        return createSimpleAdocTestFile("");
    }
    public static Path createSimpleAdocTestFile(String addition) throws IOException {
        Path adocfile = Files.createTempFile("asp_test", ".adoc");
        Files.write(adocfile, ("== Test\nThis is just a test\n"+addition).getBytes());
        return adocfile;
    }

}
