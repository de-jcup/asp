/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.asp.server.asciidoctorj.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Backend;
import de.jcup.asp.api.MapRequestParameterKey;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringRequestParameterKey;
import de.jcup.asp.api.StringResponseResultKey;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;

public class ConvertLocalFileServiceImpl implements ConvertLocalFileService {

    public static final ConvertLocalFileService INSTANCE = new ConvertLocalFileServiceImpl();
    private static final Logger LOG = LoggerFactory.getLogger(ConvertLocalFileServiceImpl.class);

    TargetFileNameProvider provider = new TargetFileNameProvider();

    ConvertLocalFileServiceImpl() {

    }

    AsciidoctorService service = AsciidoctorService.INSTANCE;

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.jcup.asp.server.asciidoctorj.service.ConvertLocalFileService#convertFile(
     * de.jcup.asp.api.Request, de.jcup.asp.api.Response)
     */
    @Override
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
        Objects.requireNonNull(filePath, "File path must be set!");

        Map<String, Object> optionsAsMap = request.getMap(MapRequestParameterKey.OPTIONS);
        LOG.debug("Options:{}", optionsAsMap);


        Asciidoctor asciidoctor = service.getAsciidoctor();
        OptionsBuilder optionsBuilder = createOptoinsBuilderWithOptionsSet(optionsAsMap);
        
        Backend backend = resolveBackend(optionsAsMap);
        optionsBuilder.backend(backend.convertToString());
        
        Map<String, Object> attributesAsMap = request.getMap(MapRequestParameterKey.ATTRIBUTES);
        LOG.debug("Attributes:{}", attributesAsMap);
        AttributesBuilder attributesBuilder =createAttributesBuilderWithAttributesSet(attributesAsMap);
        
        String graphvizDot = System.getenv("GRAPHVIZ_DOT");
        if (graphvizDot != null) {
            attributesBuilder.attribute("graphvizdot@", graphvizDot);
        }
        
        Attributes attributes = attributesBuilder.build();
        optionsBuilder.attributes(attributes);
        
        Options o = optionsBuilder.build();
        Path adocfile = Paths.get(filePath);
        asciidoctor.convertFile(adocfile.toFile(), o);

        File targetFile = provider.resolveTargetFileFor(adocfile.toFile(), backend);
        response.set(StringResponseResultKey.RESULT_FILEPATH, targetFile.getAbsolutePath());
        response.setServerLog(service.getLogDataProvider().getLogData());
    }

    private OptionsBuilder createOptoinsBuilderWithOptionsSet(Map<String, Object> optionsAsMap) {
        OptionsBuilder optionsBuilder = Options.builder();
        
        for (String option: optionsAsMap.keySet()) {
            Object value = optionsAsMap.get(option);
            
            optionsBuilder.option(option, value);
        }
        return optionsBuilder;
    }
    
    private AttributesBuilder createAttributesBuilderWithAttributesSet(Map<String, Object> attributesAsMap) {
        AttributesBuilder attributesBuilder = Attributes.builder();
        
        for (String attribute: attributesAsMap.keySet()) {
            Object value = attributesAsMap.get(attribute);
            
            attributesBuilder.attribute(attribute, value);
        }
        return attributesBuilder;
    }

    private Backend resolveBackend(Map<String, Object> optionsAsMap) {
        Object backendOption = optionsAsMap.get(Backend.getOptionId());
        Backend backend = null;
        if (backendOption instanceof String) {
            backend = Backend.convertFromString(backendOption.toString());
        } else {
            backend = Backend.getDefault();
        }
        return backend;
    }
}
