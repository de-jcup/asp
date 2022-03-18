package de.jcup.asp.server.asciidoctorj.service;

import java.util.Map;

import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Backend;
import de.jcup.asp.api.MapRequestParameterKey;
import de.jcup.asp.api.Request;

public class ConversionContextFactory {


private static final Logger LOG = LoggerFactory.getLogger(ConversionContextFactory.class);

    private RequestAndEnvironmentAttributeProvider attributeProvider;

    public ConversionContextFactory(RequestAndEnvironmentAttributeProvider attributeProvider) {
        this.attributeProvider=attributeProvider;
    }

    public ConversionContext createConversionContext(Request request) {
        
        Attributes attributes = attributeProvider.getAttributes(request);
        
        Map<String, Object> optionsAsMap = request.getMap(MapRequestParameterKey.OPTIONS);
        LOG.debug("Options:{}", optionsAsMap);
        
        OptionsBuilder optionsBuilder = createOptoinsBuilderWithOptionsSet(optionsAsMap);
        
        Backend backend = resolveBackend(optionsAsMap);
        optionsBuilder.backend(backend.convertToString());
        optionsBuilder.attributes(attributes);

        ConversionContext data = new ConversionContext();
        data.options = optionsBuilder.build();
        data.backend=backend;
     
        return data;
    }
    
    private OptionsBuilder createOptoinsBuilderWithOptionsSet(Map<String, Object> optionsAsMap) {
        OptionsBuilder optionsBuilder = Options.builder();
        
        for (String option: optionsAsMap.keySet()) {
            Object value = optionsAsMap.get(option);
            
            optionsBuilder.option(option, value);
        }
        return optionsBuilder;
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
