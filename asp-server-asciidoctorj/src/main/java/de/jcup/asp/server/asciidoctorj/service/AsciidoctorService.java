package de.jcup.asp.server.asciidoctorj.service;

import static org.asciidoctor.Asciidoctor.Factory.*;
import static org.asciidoctor.OptionsBuilder.*;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.SafeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Backend;

public class AsciidoctorService {

private static final Logger LOG = LoggerFactory.getLogger(AsciidoctorService.class);

    private Asciidoctor asciidoctor;

    public static final AsciidoctorService INSTANCE = new AsciidoctorService();
    
    private AsciidoctorService() {
        LOG.info("Starting, will create asciidoctorj instance");
        asciidoctor = create();
        LOG.info("Created instance");
    }
    
    public Asciidoctor getAsciidoctor() {
        return asciidoctor;
    }
    
    public void warmUp() {
        LOG.info("Starting warmup phase for asciidoctor");
        asciidoctor.requireLibrary("asciidoctor-diagram"); 
        asciidoctor.convert("== Just a warmup\nThis ensures asciidoctor is running and next call is faster and has now waits", options().attributes(
                AttributesBuilder.attributes()        
                .get()).backend(Backend.HTML.convertToString()).safe(SafeMode.UNSAFE).get());
        LOG.info("Warmup done");
    }

}
