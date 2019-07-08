package de.jcup.asp.server.asciidoctorj;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jcup.asp.api.Commands;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.Response;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.integrationtest.AdocTestFiles;
import de.jcup.asp.integrationtest.TestConstants;
import de.jcup.asp.integrationtest.TestOutputHandler;
import de.jcup.asp.integrationtest.TestServerSupport;
import de.jcup.asp.server.asciidoctorj.service.ConvertLocalFileService;
public class ShowCommunicationTest {

    private TestServerSupport integrationTestServersupport;
    private AspClient client;
    private TestOutputHandler testoutputhandler;
    
    @Before
    public void before() throws Exception{
        testoutputhandler=new TestOutputHandler();
        integrationTestServersupport = new TestServerSupport(new AsciiDoctorJServerClientRequestHandler() {
            @Override
            ConvertLocalFileService createConverterLocalFileService() {
                return new ConvertLocalFileService() {
                    
                    @Override
                    public void convertFile(Request request, Response response) {
                        /* just do nothing */
                    }
                };
            }
        });
        client = integrationTestServersupport.launchServerAndGetPreparedClient(TestConstants.EMBEDDED_TESTSERVER_PORT+2);
        client.setOutputHandler(testoutputhandler);
    }
    
    @After
    public void after() {
        integrationTestServersupport.getLauncher().stopServer();
    }
    
    @Test
    public void show_communication_enabled__communication_output_sent_to_outputhandler() throws Exception {
        /* prepare */
        Path path = AdocTestFiles.createSimpleAdocTestFile();
        client.setShowCommunication(true);
        
        /* execute + test */
        client.convertFile(path,Collections.singletonMap("mykey1","myvalue1"),null);
        
        /* test */
        List<String> messages = testoutputhandler.getMessages();
        assertEquals(2,messages.size());
        String message1=messages.get(0);
        String message2=messages.get(1);
        assertTrue(message1.contains(Commands.CONVERT_FILE.getId()));
        assertTrue(message1.contains("client->server"));
        assertTrue(message1.contains("mykey1"));
        assertTrue(message1.contains("myvalue1"));
        
        assertTrue(message2.contains("server->client"));
        

    }
    
    @Test
    public void default_communication_output_sent_to_outputhandler() throws Exception {
        /* prepare */
        Path path = AdocTestFiles.createSimpleAdocTestFile();
        
        /* execute + test */
        client.convertFile(path,Collections.singletonMap("mykey1","myvalue1"),null);
        
        /* test */
        List<String> messages = testoutputhandler.getMessages();
        assertEquals(0,messages.size());
        

    }
    


}
