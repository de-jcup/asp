package de.jcup.asp.server.asciidoctorj.service;

import static org.asciidoctor.OptionsBuilder.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.SafeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Backend;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.RequestParameterKeys;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.ResponseResultKeys;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;

public class ResolveAttributesService {
    
    public static final ResolveAttributesService INSTANCE = new ResolveAttributesService();
    private static final Logger LOG = LoggerFactory.getLogger(ResolveAttributesService.class);
    
    TargetFileNameProvider provider = new TargetFileNameProvider();
    
    ResolveAttributesService() {
        
    }
    
    AsciidoctorService service = AsciidoctorService.INSTANCE;

    public void resolveAttributesFromDirectory(Request request, Response response) {
        try {
            handleResolving(request, response);
        } catch (Exception e) {
            LOG.error("Was not able to resolve attributes", e);
            response.setErrorMessage(e.getMessage());
        }
    }
    
    private void handleResolving(Request request, Response response) throws Exception {
//
//        File baseDir  = request.getBaseDir();
//        String filePath = request.get(RequestParameterKeys.SOURCE_FILEPATH);
//        Objects.requireNonNull(filePath,"File path must be set!");
//        
//        Path adocfile = Paths.get(filePath);
//        // see https://github.com/asciidoctor/asciidoctorj/blob/master/docs/integrator-guide.adoc
//        service.getAsciidoctor().convertFile(adocfile.toFile(), options().attributes(
//                AttributesBuilder.attributes()        
//                .icons(Attributes.FONT_ICONS) 
//                .attribute("foo", "bar")      // (3)
//                .get()).backend(backend.convertToString()).safe(SafeMode.UNSAFE).get());
//        
//        File targetFile = provider.resolveTargetFileFor(adocfile.toFile(),backend);
//        response.set(ResponseResultKeys.RESULT_FILEPATH,targetFile.getAbsolutePath());
    }

}
