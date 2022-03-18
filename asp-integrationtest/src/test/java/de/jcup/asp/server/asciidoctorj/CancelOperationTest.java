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

import static de.jcup.asp.integrationtest.AdocTestFiles.*;
import static org.junit.Assert.*;

import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Response;
import de.jcup.asp.api.asciidoc.AsciidocAttributes;
import de.jcup.asp.api.asciidoc.AsciidocOptions;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.client.DefaultAspClientProgressMonitor;
import de.jcup.asp.integrationtest.FakeRequestHandler;
import de.jcup.asp.integrationtest.TestConstants;
import de.jcup.asp.integrationtest.TestServerSupport;
import de.jcup.asp.integrationtest.TimeAssertData;
public class CancelOperationTest {

    private static final Logger LOG = LoggerFactory.getLogger(CancelOperationTest.class);
    private FakeRequestHandler fakeRequestHandler;
    private TestServerSupport integrationTestServersupport;
    private AspClient client;
    
    @Before
    public void before() throws Exception{
        fakeRequestHandler = new FakeRequestHandler();
        integrationTestServersupport = new TestServerSupport(fakeRequestHandler);
        client = integrationTestServersupport.launchServerAndGetPreparedClient(TestConstants.EMBEDDED_TESTSERVER_PORT);
    }
    
    @After
    public void after() {
        integrationTestServersupport.getLauncher().stopServer();
    }
    
    @Test
    public void long_run_action_canceld() throws Exception {
        /* prepare */
        fakeRequestHandler.allRequestsNeedsOnServerSide(10000);
        
        /* execute + test */
        callPdfConvertAndAssertTime(client,true, TimeAssertData.max(1));
    }
    
    
    @Test
    public void long_two_actions_last_canceld() throws Exception {
        /* prepare */
        fakeRequestHandler.allRequestsNeedsOnServerSide(2000);
        
        /* execute + test */
        callPdfConvertAndAssertTime(client,false,TimeAssertData.min(2));
        callPdfConvertAndAssertTime(client,true, TimeAssertData.max(1));

    }
    
    @Test
    public void test_10_actions_all_canceld() throws Exception {
        /* prepare */
        fakeRequestHandler.allRequestsNeedsOnServerSide(8000);
        
        /* execute + test */
        for (int i=0;i<10;i++) {
            callPdfConvertAndAssertTime(client,true, TimeAssertData.max(1));
        }

    }
    
    private void callPdfConvertAndAssertTime(AspClient client, boolean autoCancel,TimeAssertData timeset) throws Exception {
        DefaultAspClientProgressMonitor monitor = new DefaultAspClientProgressMonitor();
        if (autoCancel) {
            Runnable runnable = new Runnable() {
                
                @Override
                public void run() {
                    try {
                        /* wait some time to have TCP/IP connection established*/
                        Thread.sleep(500);
                        monitor.setCanceled(true);
                        LOG.info(">> canceled by user!");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
            Thread delayedCancelByUserThread = new Thread(runnable, "Simulated (delayed) cancel by user");
            delayedCancelByUserThread.start();
            
        }
        AsciidocOptions asciidocOptions = AsciidocOptions.builder().backend("html").build();
        AsciidocAttributes asciidocAttributes = AsciidocAttributes.builder().build();
        
        /* execute */
        LOG.info("> start convert");
        long start = System.currentTimeMillis();
        
        Path adocfile = createSimpleAdocTestFile("...");
        Response response = client.convertFile(adocfile, asciidocOptions, asciidocAttributes, monitor);
        long end = System.currentTimeMillis();
        LOG.info("> end of convert call");
        LOG.info("Response:"+response.convertToString());
        /* test */
        if (autoCancel) {
            assertTrue(response.failed());
            assertTrue(response.getErrorMessage().contains("canceled"));
        }else {
            assertFalse(response.failed());
        }
        long timeInSeconds = (end-start)/1000;
        if (timeset.isMaximumAsserted()) {
            assertTrue("The pdf creation canceling must be lower/equal "+timeset.getSeconds()+" seconds!, but was:"+timeInSeconds,timeInSeconds<=timeset.getSeconds());
        }
        if (timeset.isMinimumAsserted()) {
            assertTrue("The pdf creation canceling must be higher/equal "+timeset.getSeconds()+" seconds!, but was:"+timeInSeconds,timeInSeconds>=timeset.getSeconds());
        }
    }

}
