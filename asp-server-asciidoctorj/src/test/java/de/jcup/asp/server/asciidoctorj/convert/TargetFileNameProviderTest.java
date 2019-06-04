package de.jcup.asp.server.asciidoctorj.convert;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.jcup.asp.api.Backend;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;

public class TargetFileNameProviderTest {
    private TargetFileNameProvider providerToTest;

    @Before
    public void before() {
        providerToTest = new TargetFileNameProvider();
    }

    @Test
    public void pdf_resolved() {
        assertEquals(new File("./bla.pdf"), providerToTest.resolveTargetFileFor(new File("./bla.adoc"), Backend.PDF));
    }
    @Test
    public void htmlf_resolved() {
        assertEquals(new File("./bla.html"), providerToTest.resolveTargetFileFor(new File("./bla.adoc"), Backend.HTML));
    }

}

