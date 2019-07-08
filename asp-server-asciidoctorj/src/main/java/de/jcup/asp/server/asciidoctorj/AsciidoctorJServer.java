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

import java.io.IOException;

import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;
import de.jcup.asp.server.core.ClientRequestHandler;
import de.jcup.asp.server.core.CoreAspServer;

public class AsciidoctorJServer {

    CoreAspServer coreAspServer;

    public AsciidoctorJServer() {
        coreAspServer = new CoreAspServer();
    }

    public void start(int port) throws IOException {
        if (port > 0) {
            coreAspServer.setPortNumber(port);
        }
        warmup();

        coreAspServer.setRequestHandler(createRequesthandler());

        coreAspServer.start();

    }

    protected void warmup() {
        AsciidoctorService.INSTANCE.warmUp();
    }

    protected ClientRequestHandler createRequesthandler() {
        return new AsciiDoctorJServerClientRequestHandler();
    }
    

}
