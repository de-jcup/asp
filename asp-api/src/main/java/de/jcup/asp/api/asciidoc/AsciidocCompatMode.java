package de.jcup.asp.api.asciidoc;
public enum AsciidocCompatMode {

    DEFAULT("default"),
    
    LEGACY("legacy");
    
    private String mode;
    
    private AsciidocCompatMode(String mode) {
        this.mode = mode;
    }
    
    public String getMode() {
        return mode;
    }
    
}
