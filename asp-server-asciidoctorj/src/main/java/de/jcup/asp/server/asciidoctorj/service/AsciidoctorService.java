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
package de.jcup.asp.server.asciidoctorj.service;

import static org.asciidoctor.Asciidoctor.Factory.*;
import static org.asciidoctor.OptionsBuilder.*;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.SafeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.Backend;
import de.jcup.asp.server.asciidoctorj.provider.LogDataProvider;

public class AsciidoctorService {

private static final Logger LOG = LoggerFactory.getLogger(AsciidoctorService.class);

    private Asciidoctor asciidoctor;

    private LogDataProvider logDataProvider;

    public static final AsciidoctorService INSTANCE = new AsciidoctorService();
    
    private AsciidoctorService() {
        logDataProvider = new LogDataProvider();
        
        LOG.info("Starting, will create asciidoctorj instance");
        asciidoctor = create();
        asciidoctor.registerLogHandler(logDataProvider);
        LOG.info("Created instance");
    }
    
    public Asciidoctor getAsciidoctor() {
        return asciidoctor;
    }
    
    public LogDataProvider getLogDataProvider() {
        return logDataProvider;
    }
    
    public void warmUp() {
        LOG.info("Starting warmup phase for asciidoctor");
        asciidoctor.requireLibrary("asciidoctor-diagram"); 
        asciidoctor.convert("== Just a warmup\nThis ensures asciidoctor is running and next call is faster and has now waits", options().attributes(
                AttributesBuilder.attributes()        
                .get()).backend(Backend.HTML.convertToString()).safe(SafeMode.UNSAFE).get());
        LOG.info("Warmup done");
    }

}
