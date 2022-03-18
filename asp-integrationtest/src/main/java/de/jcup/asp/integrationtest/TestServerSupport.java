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
package de.jcup.asp.integrationtest;

import de.jcup.asp.client.AspClient;
import de.jcup.asp.core.LaunchException;
import de.jcup.asp.server.asciidoctorj.AsciidoctorJServer;
import de.jcup.asp.server.asciidoctorj.EmbeddedAsciidoctorJServerLauncher;
import de.jcup.asp.server.core.ClientRequestHandler;

/**
 * Provides fake request handler
 *
 */
public class TestServerSupport {

    private EmbeddedAsciidoctorJServerLauncher launcher;
    private ClientRequestHandler handler;

    public TestServerSupport(ClientRequestHandler handler) {
        this.handler = handler;
        launcher = new TestServerLauncher();
    }

    public AspClient launchServerAndGetPreparedClient(int port) throws LaunchException {
        String key = launcher.launch(port);
        AspClient client = new AspClient(key);
        client.setPortNumber(port);
        return client;
    }

    public EmbeddedAsciidoctorJServerLauncher getLauncher() {
        return launcher;
    }

    public class TestServerLauncher extends EmbeddedAsciidoctorJServerLauncher {
        @Override
        protected AsciidoctorJServer createServer() {
            return new AsciidoctorJServer() {
                @Override
                protected void warmup() {
                    /* ignore here - speed up tests... */
                }

                @Override
                protected ClientRequestHandler createRequesthandler() {
                    return handler;
                }
            };
        }
    }
}
