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
package de.jcup.asp.server.asciidoctorj.convert;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.jcup.asp.api.Backend;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;

public class TargetFileNameProviderTest {
    private TargetFileNameProvider providerToTest;

    @Before
    public void before() {
        providerToTest = new TargetFileNameProvider();
    }

    @Test
    public void pdf_resolved() {
        assertEquals(new File("./bla.pdf"), providerToTest.resolveTargetFileFor(new File("./bla.adoc"), Backend.PDF));
    }
    @Test
    public void htmlf_resolved() {
        assertEquals(new File("./bla.html"), providerToTest.resolveTargetFileFor(new File("./bla.adoc"), Backend.HTML));
    }

}

