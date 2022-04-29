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

import de.jcup.asp.server.asciidoctorj.AsciiDoctorJServerClientRequestHandler;
import de.jcup.asp.server.core.ClientRequestHandler;
import de.jcup.asp.server.core.CoreAspServer;

public class AspServerTestMain {

    private static final Logger LOG = LoggerFactory.getLogger(AspServerTestMain.class);

    public static void main(String[] args) throws Exception {
        CoreAspServer server = new CoreAspServer();
        ClientRequestHandler requestHandler = null;
        boolean fakeAsciidoctorRequest= Boolean.getBoolean("asp.server.fakeAsciidoctorRequest");
        if (fakeAsciidoctorRequest) {
            requestHandler = new DummyOutputClientRequestHandler();
        }else {
            requestHandler = new AsciiDoctorJServerClientRequestHandler();
        }
        server.setRequestHandler(requestHandler);

        String portProperty = System.getProperty("asp.server.port");
        try {
            if (portProperty != null) {
                server.setPortNumber(Integer.parseInt(portProperty));
            }
        } catch (NumberFormatException e) {
            LOG.error("wrong port definition:{} using default", portProperty);
        }
        server.start();
    }
}
