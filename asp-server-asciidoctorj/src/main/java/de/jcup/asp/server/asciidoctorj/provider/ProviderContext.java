package de.jcup.asp.server.asciidoctorj.provider;

import java.io.File;

import org.asciidoctor.Asciidoctor;

public interface ProviderContext {

    Asciidoctor getAsciiDoctor();

    File getBaseDir();

}