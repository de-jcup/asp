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

public enum AsciidocSafeMode {

    /**
     * A safe mode level that disables all security features enforced by Asciidoctor 
     */
    UNSAFE(0),
    /**
     * A safe mode level that closely parallels safe mode in AsciiDoc. This value prevents access to files which reside
     * outside of the parent directory of the source file and disables any macro other than the include::[] macro.
     */
    SAFE(1),
    /**
     * A safe mode level that disallows the document from setting attributes that would affect the rendering of the
     * document, in addition to all the security features of AsciidocSafeMode::SAFE. For instance, this level disallows changing
     * the backend or the source-highlighter using an attribute defined in the source document. This is the most
     * fundamental level of security for server-side deployments (hence the name).
     */
    SERVER(10),
    /**
     * A safe mode level that disallows the document from attempting to read files from the file system and including
     * the contents of them into the document, in additional to all the security features of AsciidocSafeMode::SERVER. For
     * instance, this level disallows use of the include::[] macro and the embedding of binary content (data uri),
     * stylesheets and JavaScripts referenced by the document.(Asciidoctor and trusted extensions may still be allowed
     * to embed trusted content into the document).
     *
     * Since Asciidoctor is aiming for wide adoption, this level is the default and is recommended for server-side
     * deployments.
     */
    SECURE(20);

    private int level;

    private AsciidocSafeMode(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static final AsciidocSafeMode asciidocSafeMode(int level) {
        switch(level) {
            case 0: return UNSAFE;
            case 1: return SAFE;
            case 10: return SERVER;
            default: return SECURE;
        }
    }

}
