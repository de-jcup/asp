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
package de.jcup.asp.integrationtest.testapplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Command;
import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.api.StringResponseResultKey;
import de.jcup.asp.server.core.ClientRequestHandler;

public class DummyOutputClientRequestHandler implements ClientRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DummyOutputClientRequestHandler.class);

    @Override
    public Response handleRequest(Request request) {
        LOG.info("Request:\n" + request.convertToString());
        Command command = request.getCommand();
        Response response = new Response();
        if (Commands.CONVERT_FILE.equals(command)) {
            response.set(StringResponseResultKey.RESULT_FILEPATH, "~/theresult");
        } else {
            response.setErrorMessage("Unsupported command:" + command);
        }
        return response;
    }

}
