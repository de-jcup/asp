package de.jcup.asp.api.asciidoc;

public enum AsciidocOption {

    IN_PLACE("in_place"),

    ATTRIBUTES("attributes"),

    HEADER_FOOTER("header_footer"),

    TEMPLATE_DIRS("template_dirs"),

    TEMPLATE_ENGINE("template_engine"),

    TO_FILE("to_file"),

    TO_DIR("to_dir"),

    MKDIRS("mkdirs"),

    SAFE("safe"),

    SOURCEMAP("sourcemap"),

    STANDALONE("standalone"),

    ERUBY("eruby"),

    CATALOG_ASSETS("catalog_assets"),

    COMPACT("compact"),

    DESTINATION_DIR("destination_dir"),

    SOURCE_DIR("source_dir"),

    BACKEND("backend"),

    DOCTYPE("doctype"),

    BASEDIR("base_dir"),

    TEMPLATE_CACHE("template_cache"),

    SOURCE("source"),

    PARSE("parse"),

    PARSE_HEADER_ONLY("parse_header_only");

    private String key;

    private AsciidocOption(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
