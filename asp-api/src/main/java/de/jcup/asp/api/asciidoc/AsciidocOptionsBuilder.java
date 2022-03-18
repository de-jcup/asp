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
package de.jcup.asp.api.asciidoc;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

public class AsciidocOptionsBuilder {

    private final AsciidocOptions asciidocOptions = new AsciidocOptions();

    AsciidocOptionsBuilder() {
        super();
    }

    /**
     * Sets backend option.
     * 
     * @param backend
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder backend(String backend) {
        this.asciidocOptions.setBackend(backend);
        return this;
    }

    /**
     * Sets doctype option.
     * 
     * @param docType
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder docType(String docType) {
        this.asciidocOptions.setDocType(docType);
        return this;
    }

    /**
     * Sets in place attribute.
     * 
     * @param inPlace
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder inPlace(boolean inPlace) {
        this.asciidocOptions.setInPlace(inPlace);
        return this;
    }

    /**
     * Sets header footer attribute.
     * 
     * @param headerFooter
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder headerFooter(boolean headerFooter) {
        this.asciidocOptions.setHeaderFooter(headerFooter);
        return this;
    }

    /**
     * Sets template directory.
     *
     * @deprecated Use {@link #templateDirs(File...)} instead.
     *
     * @param templateDir
     *            directory where templates are stored.
     * @return this instance.
     */
    public AsciidocOptionsBuilder templateDir(File templateDir) {
        this.asciidocOptions.setTemplateDirs(templateDir.getAbsolutePath());
        return this;
    }

    /**
     * Sets template directories.
     * 
     * @param templateDirs
     *            directories where templates are stored.
     * @return this instance.
     */
    public AsciidocOptionsBuilder templateDirs(File... templateDirs) {
        for (File templateDir : templateDirs) {
            this.asciidocOptions.setTemplateDirs(templateDir.getAbsolutePath());
        }
        return this;
    }

    /**
     * Sets the template engine.
     * 
     * @param templateEngine
     *            used to render the document.
     * @return this instance.
     */
    public AsciidocOptionsBuilder templateEngine(String templateEngine) {
        this.asciidocOptions.setTemplateEngine(templateEngine);
        return this;
    }

    /**
     * Sets if Asciidoctor should use template cache or not.
     * 
     * @param templateCache
     *            true if template cache is required, false otherwise.
     * @return this instance.
     */
    public AsciidocOptionsBuilder templateCache(boolean templateCache) {
        this.asciidocOptions.setTemplateCache(templateCache);
        return this;
    }

    /**
     * Sets attributes used for rendering input.
     * @deprecated Use {@link #asciidocAttributes(AsciidocAttributes)} instead.
     * 
     * @param attributes
     *            map.
     * @return this instance.
     */
    @Deprecated
    public AsciidocOptionsBuilder asciidocAttributes(Map<String, Object> attributes) {
        this.asciidocOptions.setAttributes(attributes);
        return this;
    }

    /**
     * Sets attributes used for rendering input.
     * 
     * @param asciidocAttributes
     *            map.
     * @return this instance.
     */
    public AsciidocOptionsBuilder asciidocAttributes(AsciidocAttributes asciidocAttributes) {
        this.asciidocOptions.setAttributes(asciidocAttributes);
        return this;
    }
    
    /**
     * Sets to file value. This is the destination file name.
     * 
     * @param toFile
     *            name of output file.
     * @return this instance.
     */
    public AsciidocOptionsBuilder toFile(File toFile) {
        this.asciidocOptions.setToFile(toFile.getPath());
        return this;
    }

    public AsciidocOptionsBuilder toStream(OutputStream toStream) {
        this.asciidocOptions.setToStream(toStream);
        return this;
    }

    /**
     * Sets to dir value. This is the destination directory.
     * 
     * @param directory
     *            where output is generated.
     * @return this instance.
     */
    public AsciidocOptionsBuilder toDir(File directory) {
        this.asciidocOptions.setToDir(directory.getAbsolutePath());
        return this;
    }

    /**
     * Sets if asciidoctor should create output directory if it does not exist or not.
     * 
     * @param mkDirs
     *            true if directory must be created, false otherwise.
     * @return this instance.
     */
    public AsciidocOptionsBuilder mkDirs(boolean mkDirs) {
        this.asciidocOptions.setMkDirs(mkDirs);
        return this;
    }

    /**
     * Sets the safe mode.
     * 
     * @param asciidocSafeMode
     *            to run asciidoctor.
     * @return this instance.
     */
    public AsciidocOptionsBuilder safe(AsciidocSafeMode asciidocSafeMode) {
        this.asciidocOptions.setSafe(asciidocSafeMode);
        return this;
    }

    /**
     * Keeps track of the file and line number for each parsed block. (Useful for tooling applications where the association between the converted output and the source file is important).
     * 
     * @param sourcemap
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder sourcemap(boolean sourcemap) {
        this.asciidocOptions.setSourcemap(sourcemap);
        return this;
    }

    /**
     * Sets eruby implementation.
     * 
     * @param eruby
     *            implementation.
     * @return this instance.
     */
    public AsciidocOptionsBuilder eruby(String eruby) {
        this.asciidocOptions.setEruby(eruby);
        return this;
    }
    
    /**
     * If true, tells the parser to capture images and links in the reference table. (Normally only IDs, footnotes and indexterms are included). The reference table is available via the references property on the document AST object. (Experimental).
     * 
     * @param catalogAssets
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder catalogAssets(boolean catalogAssets) {
        this.asciidocOptions.setCatalogAssets(catalogAssets);
        return this;
    }

    /**
     * Compact the output removing blank lines.
     * 
     * @param compact
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder compact(boolean compact) {
        this.asciidocOptions.setCompact(compact);
        return this;
    }

    /**
     * If true, the source is parsed eagerly (i.e., as soon as the source is passed to the load or load_file API). If false, parsing is deferred until the parse method is explicitly invoked.
     * 
     * @param parse
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder parse(boolean parse) {
        this.asciidocOptions.setParse(parse);
        return this;
    }

    /**
     * Sets parse header only falg.
     * 
     * @param parseHeaderOnly
     *            value.
     * @return this instance.
     */
    public AsciidocOptionsBuilder parseHeaderOnly(boolean parseHeaderOnly) {
        this.asciidocOptions.setParseHeaderOnly(parseHeaderOnly);
        return this;
    }

    /**
     * Destination output directory.
     * 
     * @param destinationDir
     *            destination directory.
     * @return this instance.
     */
    public AsciidocOptionsBuilder destinationDir(File destinationDir) {
        this.asciidocOptions.setDestinationDir(destinationDir.getAbsolutePath());
        return this;
    }

    /**
     * Source directory.
     *
     * This must be used alongside {@link #destinationDir(File)}.
     *
     * @param srcDir
     *            source directory.
     * @return this instance.
     */
    public AsciidocOptionsBuilder sourceDir(File srcDir) {
        this.asciidocOptions.setSourceDir(srcDir.getAbsolutePath());
        return this;
    }
    
    /**
     * Sets base dir for working directory.
     * 
     * @param baseDir
     *            working directory.
     * @return this instance.
     */
    public AsciidocOptionsBuilder baseDir(File baseDir) {
        this.asciidocOptions.setBaseDir(baseDir.getAbsolutePath());
        return this;
    }
    
    /**
     * Returns a valid AsciidocOptions instance.
     * 
     * @return asciidocOptions instance.
     */
    public AsciidocOptions build() {
        return this.asciidocOptions;
    }

    public AsciidocOptionsBuilder customOption(String option, Object value) {
        this.asciidocOptions.setCustomOption(option,value);
        return this;
    }

}
