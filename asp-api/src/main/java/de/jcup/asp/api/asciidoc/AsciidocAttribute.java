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

public enum AsciidocAttribute {

    TOC("toc"),

    TOC_POSITION("toc-position"),

    TOC_LEVELS("toclevels"),

    TOC_2("toc2"),

    TITLE("title"),

    IMAGESDIR("imagesdir"),

    SOURCE_HIGHLIGHTER("source-highlighter"),

    SOURCE_LANGUAGE("source-language"),

    LOCALDATE("localdate"),

    LOCALTIME("localtime"),

    DOCDATE("docdate"),

    DOCTIME("doctime"),

    STYLESHEET_NAME("stylesheet"),

    STYLES_DIR("stylesdir"),

    NOT_STYLESHEET_NAME("stylesheet!"),

    LINK_CSS("linkcss"),

    COPY_CSS("copycss"),

    ICONS("icons"),

    ICONFONT_REMOTE("iconfont-remote"),

    ICONFONT_CDN("iconfont-cdn"),

    ICONFONT_NAME("iconfont-name"),

    ICONS_DIR("iconsdir"),

    DATA_URI("data-uri"),

    SECTION_NUMBERS("numbered"),

    IMAGE_ICONS(""),

    FONT_ICONS("font"),

    LINK_ATTRS("linkattrs"),

    EXPERIMENTAL("experimental"),

    SHOW_TITLE("showtitle"),

    NOTITLE("notitle"),

    ALLOW_URI_READ("allow-uri-read"),

    IGNORE_UNDEFINED("ignore-undefined"),

    UNTITLED_LABEL("untitled-label"),

    SET_ANCHORS("sectanchors"),

    SKIP_FRONT_MATTER("skip-front-matter"),

    MAX_INCLUDE_DEPTH("max-include-depth"),

    ATTRIBUTE_MISSING("attribute-missing"),

    ATTRIBUTE_UNDEFINED("attribute-undefined"),

    NO_FOOTER("nofooter"),

    HARDBREAKS("hardbreaks"),

    SECT_NUM_LEVELS("sectnumlevels"),

    CACHE_URI("cache-uri"),

    MATH("stem"),

    APPENDIX_CAPTION("appendix-caption"),

    HIDE_URI_SCHEME("hide-uri-scheme"),

    COMPAT_MODE("compat-mode");

    private String key;

    private AsciidocAttribute(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
