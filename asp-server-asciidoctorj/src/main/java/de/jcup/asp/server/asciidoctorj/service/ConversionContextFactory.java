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
        LOG.debug("AsciidocOptions:{}", optionsAsMap);
        
        OptionsBuilder optionsBuilder = createOptionsBuilderWithOptionsSet(optionsAsMap);
        
        Backend backend = resolveBackend(optionsAsMap);
        optionsBuilder.backend(backend.convertToString());
        optionsBuilder.attributes(attributes);

        ConversionContext data = new ConversionContext();
        data.options = optionsBuilder.build();
        data.options.setAttributes(attributes);
        data.backend=backend;
     
        return data;
    }
    
    private OptionsBuilder createOptionsBuilderWithOptionsSet(Map<String, Object> optionsAsMap) {
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
