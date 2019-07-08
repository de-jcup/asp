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
        response.setServerLog(service.getLogDataProvider().getLogData());
    }

}
