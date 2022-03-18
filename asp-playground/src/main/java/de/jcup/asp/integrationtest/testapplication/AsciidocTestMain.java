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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import de.jcup.asp.api.Backend;
import de.jcup.asp.server.asciidoctorj.provider.TargetFileNameProvider;
import de.jcup.asp.server.asciidoctorj.service.AsciidoctorService;

/**
 * Just a playground to use asciidoctorj directly and to check the outputs
 * 
 * @author albert
 *
 */
public class AsciidocTestMain {

    public static void main(String[] args) throws Exception {
        System.out.println("Creating asciidoctor instance");
        Asciidoctor asciidoctor = AsciidoctorService.INSTANCE.getAsciidoctor();
        System.out.println("Creating tmp file");
        Path adocfile = Files.createTempFile("test", ".adoc");
        Files.newBufferedWriter(adocfile).append("== Interests in asciidoc\nsomething important...\n\nTIP: Do not write word, but asciidoc ;-)").close();

        System.out.println("Convert tmp file");
        // see
        // https://github.com/asciidoctor/asciidoctorj/blob/master/docs/integrator-guide.adoc
        String styleDirectory = "styles2";
        
//        convert(asciidoctor, adocfile, Backend.HTML, "my-stylesheet.css", styleDirectory); // works with HTML
        convert(asciidoctor, adocfile, Backend.PDF, "additional-theme.yml", styleDirectory);
        System.exit(0);
    }

    private static void convert(Asciidoctor asciidoctor, Path adocfile, Backend backend, String styleSheetName, String styleDirectory) throws IOException {
        /* @formatter:off */
        OptionsBuilder builder = Options.builder().
                backend(backend.convertToString()).
                safe(SafeMode.UNSAFE);

        File stylesDirectory = new File("./src/main/resources/"+styleDirectory);
        if (!stylesDirectory.exists()) {
            throw new IllegalStateException(stylesDirectory + " does not exist!");
        }
        String stylesDir = stylesDirectory.toPath().toRealPath().toFile().getAbsolutePath();
        System.out.println("using styles dir:"+stylesDir);
        
        
        Attributes attributes = Attributes.builder().
                icons(Attributes.FONT_ICONS).
                attribute("foo", "bar").
                stylesDir(stylesDir).
                styleSheetName(styleSheetName).
                
                /* special handling for pdf necessary: details, see https://github.com/asciidoctor/asciidoctor-pdf/blob/v1.6.0/docs/theming-guide.adoc#keys-extends*/
                attribute("pdf-theme",styleSheetName).  
                attribute("pdf-themesdir",stylesDir). // --> works
//                attribute("page-background-image","my-logo.png"). would not work, results in: WARNING: page background image not found or readable: /tmp/my-logo.png
                
                build();
        /* @formatter:on */

        Options options = builder.attributes(attributes).build();
        
        asciidoctor.convertFile(adocfile.toFile(), options);
        
        TargetFileNameProvider provider = new TargetFileNameProvider();
        
        File resolveTargetFileFor = provider.resolveTargetFileFor(adocfile.toFile(),backend);
        System.out.println("Open converted file:"+resolveTargetFileFor);
        Desktop.getDesktop().open(resolveTargetFileFor);
        System.out.println("opened "+backend.name()+" in external view");
    }
}
