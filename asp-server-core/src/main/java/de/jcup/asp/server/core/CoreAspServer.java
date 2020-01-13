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
package de.jcup.asp.server.core;

import static de.jcup.asp.core.CoreConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.core.Constants;
import de.jcup.asp.core.CryptoAccess;

public class CoreAspServer {

    private static final Logger LOG = LoggerFactory.getLogger(CoreAspServer.class);

    private int portNumber = Constants.DEFAULT_SERVER_PORT;
    private ClientRequestHandler requestHandler;

    private CryptoAccess cryptoAccess;
    private ServerSocket serverSocket;
    private boolean ready;
    private boolean stoppedByApiCall;

    public boolean isReady() {
        return ready;
    }

    public CoreAspServer() {
        this.cryptoAccess = new CryptoAccess();
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public void setRequestHandler(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public CryptoAccess getCryptoAccess() {
        return cryptoAccess;
    }

    public void start() throws IOException {
        Objects.requireNonNull(requestHandler, "Request handler not set!");
        LOG.info("Server starting at port:{}", portNumber);

        try (ServerSocket serverSocket = new ServerSocket(portNumber, 0, InetAddress.getLoopbackAddress())) {
            this.serverSocket = serverSocket;
            LOG.info(SERVER_SECRET_OUTPUT_PREFIX + "{}", cryptoAccess.getSecretKey());
            ready = true;
            while (true) {
                try {
                    waitForClient();
                } catch (SocketException e) {
                    if (stoppedByApiCall) {
                        return;
                    }
                    LOG.error("Server socket communication broken", e);
                    throw e;
                } catch (Exception e) {
                    LOG.error("Client communication failed", e);
                }
            }
        }

    }

    /* read lines from client, until Request.TERMINATOR is send */
    private void waitForClient() throws Exception {
        LOG.debug("Server waiting for client call");
        try (Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"))) {

            StringBuilder sb = new StringBuilder();
            String encryptedFromClient = null;
            while ((encryptedFromClient = in.readLine()) != null) {
                if (Request.TERMINATOR.equals(encryptedFromClient)) {
                    break;
                }
                String decryptedFromClient = cryptoAccess.decrypt(encryptedFromClient);
                sb.append(decryptedFromClient);
                sb.append('\n');
            }
            Response response = null;
            try {
                Request request = Request.convertFromString(sb.toString());
                if (Commands.IS_ALIVE.equals(request.getCommand())) {
                    /* we send back just an empty response */
                    response = new Response();
                } else {
                    /* all other commands are handled by request handler */
                    response = requestHandler.handleRequest(request);
                }

            } catch (Exception e) {
                LOG.error("Request handling failed", e);
                response = new Response();
                response.setErrorMessage(e.getMessage());
            }
            out.println(cryptoAccess.encrypt(response.convertToString()));
            out.println(Response.TERMINATOR);

        }
    }

    public void stop() {
        stoppedByApiCall=true;
        if (serverSocket == null) {
            return;
        }
        if (serverSocket.isClosed()) {
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot close server socket", e);
        }
    }
}
