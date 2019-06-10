package de.jcup.asp.server.asciidoctorj.service;

import java.io.File;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.MapResponseResultKey;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.StringRequestParameterKey;
import de.jcup.asp.api.Response;
import de.jcup.asp.server.asciidoctorj.provider.AsciiDoctorAttributesProvider;
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

        File baseDir  = request.getBaseDir();
        Objects.requireNonNull(baseDir,"File path must be set!");
        
        String filePath = request.getString(StringRequestParameterKey.SOURCE_FILEPATH);
        Objects.requireNonNull(filePath,"File path must be set!");
        
        AsciiDoctorAttributesProvider provider = new AsciiDoctorAttributesProvider(service.getAsciidoctor());
        
        Map<String, Object> map = provider.getCachedAttributes(baseDir);
        
        response.set(MapResponseResultKey.RESULT_ATTRIBUTES,map);
    }

}
