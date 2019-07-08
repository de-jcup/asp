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

import static de.jcup.asp.core.ServerExitCodes.*;

import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.core.Constants;
import de.jcup.asp.core.ServerExitCodes;

public class AsciidoctorJServerApplication {

    private static final String SYSTEM_PROPERTY__ASP_SERVER_PORT = "asp.server.port";
    private static final Logger LOG = LoggerFactory.getLogger(AsciidoctorJServerApplication.class);

    public static void main(final String[] args) {
        String portProperty = System.getProperty(SYSTEM_PROPERTY__ASP_SERVER_PORT);
        int port=-1;
        try {
            if (portProperty != null) {
                port=Integer.parseInt(portProperty);
            }
        } catch (NumberFormatException e) {
            port = Constants.DEFAULT_SERVER_PORT;
            LOG.error("Wrong port definition:{} using default:{}", portProperty,port);
        }    
        AsciidoctorJServer server = new AsciidoctorJServer();
        try {
            server.start(port);
        }catch(BindException be) {
            if (port==-1) {
                LOG.error("Already bind port:{}",port);
            }
            exit(ERROR_PORT_ALREADY_USED);
        }catch (Exception e) {
            LOG.error("Server cannot be started", e);
            exit(ERROR);
        }
    }

    private static void exit(ServerExitCodes error) {
         System.exit(error.getExitCode());
    }

}
