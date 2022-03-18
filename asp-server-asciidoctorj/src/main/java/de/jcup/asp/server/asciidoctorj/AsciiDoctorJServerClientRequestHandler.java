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
package de.jcup.asp.server.asciidoctorj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringResponseResultKey;
import de.jcup.asp.server.asciidoctorj.service.ConvertLocalFileService;
import de.jcup.asp.server.asciidoctorj.service.ConvertLocalFileServiceImpl;
import de.jcup.asp.server.core.ClientRequestHandler;

public class AsciiDoctorJServerClientRequestHandler implements ClientRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AsciiDoctorJServerClientRequestHandler.class);
    private ConvertLocalFileService converterLocalFileService;

    public AsciiDoctorJServerClientRequestHandler() {
        this.converterLocalFileService = createConverterLocalFileService();
    }
    
    @Override
    public Response handleRequest(Request request) {
        LOG.debug("Request:\n" + request.convertToString());
        Command command = request.getCommand();
        Response response = new Response();
        response.set(StringResponseResultKey.VERSION, Version.getVersion()); 

        if (Commands.CONVERT_FILE.equals(command)) {
            converterLocalFileService.convertFile(request, response);
        } else {
            response.setErrorMessage("Unsupported command:" + command);
        }
        return response;

    }
    
    ConvertLocalFileService createConverterLocalFileService() {
        return ConvertLocalFileServiceImpl.INSTANCE;
    }

}
