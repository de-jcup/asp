package de.jcup.asp.server.asciidoctorj.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import org.asciidoctor.Asciidoctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Backend;
import de.jcup.asp.api.MapRequestParameterKey;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringRequestParameterKey;
import de.jcup.asp.api.StringResponseResultKey;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;

public class ConvertLocalFileService {
    
    public static final ConvertLocalFileService INSTANCE = new ConvertLocalFileService();
    private static final Logger LOG = LoggerFactory.getLogger(ConvertLocalFileService.class);
    
    TargetFileNameProvider provider = new TargetFileNameProvider();
    
    ConvertLocalFileService() {
        
    }
    
    AsciidoctorService service = AsciidoctorService.INSTANCE;

    public void convertFile(Request request, Response response) {
        try {
            handleConvert(request, response);
        } catch (Exception e) {
            LOG.error("Was not able to convert", e);
            response.setErrorMessage(e.getMessage());
        }
    }
    
    private void handleConvert(Request request, Response response) throws Exception {

        String filePath = request.getString(StringRequestParameterKey.SOURCE_FILEPATH);
        Objects.requireNonNull(filePath,"File path must be set!");
        
        Map<String, Object> options = request.getMap(MapRequestParameterKey.OPTIONS);
        LOG.debug("Options:{}",options);
        Object backendOption = options.get(Backend.getOptionId());
        Backend backend = null;
        if (backendOption instanceof String) {
            backend  = Backend.convertFromString(backendOption.toString());
        }else {
            backend = Backend.getDefault();
            options.put(Backend.getOptionId(), backend.convertToString());
        }
        
        Path adocfile = Paths.get(filePath);
        // see https://github.com/asciidoctor/asciidoctorj/blob/master/docs/integrator-guide.adoc
        Asciidoctor asciidoctor = service.getAsciidoctor();
        asciidoctor.convertFile(adocfile.toFile(), options);
        
        File targetFile = provider.resolveTargetFileFor(adocfile.toFile(),backend);
        response.set(StringResponseResultKey.RESULT_FILEPATH,targetFile.getAbsolutePath());
        response.setServerLog(service.getLogDataProvider().getLogData());
    }
}
