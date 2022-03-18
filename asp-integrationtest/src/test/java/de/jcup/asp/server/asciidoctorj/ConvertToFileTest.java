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

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.jcup.asp.api.Response;
import de.jcup.asp.api.asciidoc.AsciidocAttributes;
import de.jcup.asp.api.asciidoc.AsciidocOptions;
import de.jcup.asp.client.AspClient;
import de.jcup.asp.integrationtest.TestConstants;
import de.jcup.asp.integrationtest.TestFileReader;

public class ConvertToFileTest {

    private static int port;
    private static EmbeddedAsciidoctorJServerLauncher launcherUsedForTesting;

    @Rule
    public Timeout timeOutRule = Timeout.seconds(2220);
    private static String key;
    private static AspClient client;

    @BeforeClass
    public static void before() throws Exception {
        port = TestConstants.EMBEDDED_TESTSERVER_PORT + 2;
        launcherUsedForTesting = new EmbeddedAsciidoctorJServerLauncher();
        key = launcherUsedForTesting.launch(port);
        client = new AspClient(key);
        client.setPortNumber(port);
    }

    @AfterClass
    public static void after() {
        launcherUsedForTesting.stopServer();
    }

    @Test
    public void convert_file_to_html_works() throws Exception {
        /* prepare */

        Path adocfile = createSimpleAdocTestFile("To check html works...");
        AsciidocOptions asciidocOptions = AsciidocOptions.builder().backend("html").build();
        AsciidocAttributes asciidocAttributes = AsciidocAttributes.builder().build();

        /* execute */
        Response response = client.convertFile(adocfile, asciidocOptions, asciidocAttributes, null);

        /* test */
        assertFalse(response.failed());
        Path result = response.getResultFilePath();
        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().getName().endsWith(".html"));

        String content = TestFileReader.readTextfile(result.toFile());
        assertTrue(content.contains("Last updated"));
        assertFalse(content.contains("Last updated 1970-01-01")); // check that this is not the text of other test where we check attribute is
                                                                  // handled
    }

    @Test
    public void convert_file_to_html_works__attribute_date_set_to_1970_01_01() throws Exception {
        /* prepare */

        Path adocfile = createSimpleAdocTestFile("asciidoc attribute show title=false, html");
        AsciidocOptions asciidocOptions = AsciidocOptions.builder().backend("html").build();

        Date date = new Date(0);

        AsciidocAttributes asciidocAttributes = AsciidocAttributes.builder().showTitle(false).docDate(date).build();

        /* execute */
        Response response = client.convertFile(adocfile, asciidocOptions, asciidocAttributes, null);

        /* test */
        assertFalse(response.failed());
        Path result = response.getResultFilePath();
        System.out.println("result:" + result);
        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().getName().endsWith(".html"));

        String content = TestFileReader.readTextfile(result.toFile());
        System.out.println(content);
        assertTrue(content.contains("Last updated 1970-01-01"));

    }

    @Test
    public void convert_file_to_pdf_works_with_asciidoc_options_pdf() throws Exception {
        /* prepare */

        Path adocfile = createSimpleAdocTestFile("To check pdf works...");

        AsciidocOptions asciidocOptions = AsciidocOptions.builder().backend("pdf").build();
        AsciidocAttributes asciidocAttributes = AsciidocAttributes.builder().build();

        /* execute */
        Response response = client.convertFile(adocfile, asciidocOptions, asciidocAttributes, null);

        /* test */
        assertFalse(response.failed());
        Path result = response.getResultFilePath();
        System.out.println("result:" + result);
        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().getName().endsWith(".pdf"));

    }

    private Path createSimpleAdocTestFile(String addition) throws IOException {
        Path adocfile = Files.createTempFile("asp_test", ".adoc");
        Files.write(adocfile, ("== Test\nThis is just a test\n" + addition).getBytes());
        return adocfile;
    }

}
