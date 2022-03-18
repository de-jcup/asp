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
import static de.jcup.asp.api.asciidoc.AsciidocOption.*;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AsciidocOptions {

    
    private Map<String, Object> options = new HashMap<>();

    AsciidocOptions() {
        super();
    }

    public static AsciidocOptionsBuilder builder() {
        return new AsciidocOptionsBuilder();
    }
    
    public void setInPlace(boolean inPlace) {
        this.put(IN_PLACE, inPlace);
    }

    private void put(AsciidocOption asciidocOption, Object value) {
        this.options.put(asciidocOption.getKey(), value);
    }
    
    public void setAttributes(AsciidocAttributes asciidocAttributes) {
        this.put(ATTRIBUTES, asciidocAttributes.toMap());
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.put(ATTRIBUTES, attributes);
    }

    /**
     * Toggle including header and footer into the output.
     *
     * @param headerFooter If <code>true</true>, include header and footer into the output,
     *                     otherwise exclude them. This overrides any output-specific defaults.
     *
     */
    public void setHeaderFooter(boolean headerFooter) {
        this.put(HEADER_FOOTER, headerFooter);
    }

    public void setTemplateDirs(String... templateDirs) {

        if (!this.options.containsKey(TEMPLATE_DIRS.getKey())) {
            this.put(TEMPLATE_DIRS, new ArrayList<Object>());
        }

        @SuppressWarnings("unchecked")
        List<Object> allTemplateDirs = (List<Object>) this.options.get(TEMPLATE_DIRS.getKey());
        allTemplateDirs.addAll(Arrays.asList(templateDirs));
    }

    public void setTemplateEngine(String templateEngine) {
        this.put(TEMPLATE_ENGINE, templateEngine);
    }

    /**
     * Enable writing output to a file. The file includes header and footer by default.
     *
     * @param toFile The path to the output file. If the path is not absolute, it is
     *               interpreted relative to what was set via {@link #setToDir(String)}}
     *               or {@link #setBaseDir(String)}}, in that order.
     *
     */
    public void setToFile(String toFile) {
        this.put(TO_FILE, toFile);
    }

    public void setToStream(OutputStream toStream) {
        this.put(TO_FILE, toStream);
    }

    /**
     * Toogle writing output to a file.
     *
     * @param toFile If <code>true</true>, write output to a file in the same directory
     *               as the input file, including header and footer into the output. If
     *               <code>false</code>, return output as a string without any header or
     *               footer. The default header and footer visibility can be overridden
     *               using {@link #setHeaderFooter(boolean)}.
     *
     */
    public void setToFile(boolean toFile) {
        this.put(TO_FILE, toFile);
    }

    public void setToDir(String toDir) {
        this.put(TO_DIR, toDir);
    }

    public void setMkDirs(boolean mkDirs) {
        this.put(MKDIRS, mkDirs);
    }

    /**
     * Safe method calls safeMode.getLevel() to put the required level.
     * 
     * @param asciidocSafeMode
     *            enum.
     */
    public void setSafe(AsciidocSafeMode asciidocSafeMode) {
        this.put(SAFE, asciidocSafeMode.getLevel());
    }

    /**
     * Keeps track of the file and line number for each parsed block. (Useful for tooling applications where the association between the converted output and the source file is important).
     * 
     * @param sourcemap
     *            value.
     */
    public void setSourcemap(boolean sourcemap) {
        this.put(SOURCEMAP, sourcemap);
    }

    public void setEruby(String eruby) {
        this.put(ERUBY, eruby);
    }

    /**
     * If true, tells the parser to capture images and links in the reference table. (Normally only IDs, footnotes and indexterms are included). The reference table is available via the references property on the document AST object. (Experimental).
     * 
     * @param catalogAssets
     *            value.
     */
    public void setCatalogAssets(boolean catalogAssets) {
        this.put(CATALOG_ASSETS, catalogAssets);
    }

    public void setCompact(boolean compact) {
        this.put(COMPACT, compact);
    }

    public void setDestinationDir(String destinationDir) {
        this.put(DESTINATION_DIR, destinationDir);
    }

    public void setSourceDir(String srcDir) {
        this.put(SOURCE_DIR, srcDir);
    }

    public void setBackend(String backend) {
        this.put(BACKEND, backend);
    }

    public void setDocType(String docType) {
        this.put(DOCTYPE, docType);
    }

    public void setBaseDir(String baseDir) {
        this.put(BASEDIR, baseDir);
    }

    public void setTemplateCache(boolean templateCache) {
        this.put(TEMPLATE_CACHE, templateCache);
    }

    /**
     * If true, the source is parsed eagerly (i.e., as soon as the source is passed to the load or load_file API). If false, parsing is deferred until the parse method is explicitly invoked.
     * 
     * @param parse
     *            value.
     */
    public void setParse(boolean parse) {
        this.put(PARSE, parse);
    }

    public void setParseHeaderOnly(boolean parseHeaderOnly) {
        this.put(PARSE_HEADER_ONLY, parseHeaderOnly);
    }

    /**
     * 
     * @return unmodifiable presentation as map
     */
    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(this.options);
    }

    public void setCustomOption(String option, Object value) {
        this.options.put(option, value);
    }

}