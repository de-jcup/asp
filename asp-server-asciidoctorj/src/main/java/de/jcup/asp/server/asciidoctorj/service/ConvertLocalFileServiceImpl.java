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
import java.util.Objects;

import org.asciidoctor.Asciidoctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringRequestParameterKey;
import de.jcup.asp.api.StringResponseResultKey;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;

public class ConvertLocalFileServiceImpl implements ConvertLocalFileService {

    public static final ConvertLocalFileService INSTANCE = new ConvertLocalFileServiceImpl();
    private static final Logger LOG = LoggerFactory.getLogger(ConvertLocalFileServiceImpl.class);

    TargetFileNameProvider targetFileNameProvider;
    ConversionContextFactory conversionContextFactory;

    ConvertLocalFileServiceImpl() {
        targetFileNameProvider = new TargetFileNameProvider();
        conversionContextFactory = new ConversionContextFactory(new RequestAndEnvironmentAttributeProvider());
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


        Asciidoctor asciidoctor = service.getAsciidoctor();
          
        Path adocfile = Paths.get(filePath);
        
        ConversionContext conversionContext = conversionContextFactory.createConversionContext(request);
        
        asciidoctor.convertFile(adocfile.toFile(), conversionContext.options);

        File targetFile = targetFileNameProvider.resolveTargetFileFor(adocfile.toFile(), conversionContext.backend);
        
        response.set(StringResponseResultKey.RESULT_FILEPATH, targetFile.getAbsolutePath());
        response.setServerLog(service.getLogDataProvider().getLogData());
    }


   
}
