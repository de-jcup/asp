package de.jcup.asp.server.asciidoctorj.provider;

import java.io.File;

import org.asciidoctor.Asciidoctor;

import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;

public class ProviderContextImpl implements ProviderContext {

    private File baseDir;

    @Override
    public Asciidoctor getAsciiDoctor() {
        return AsciidoctorService.INSTANCE.getAsciidoctor();
    }
    
    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }
    
    @Override
    public File getBaseDir() {
        return baseDir;
    }
}
