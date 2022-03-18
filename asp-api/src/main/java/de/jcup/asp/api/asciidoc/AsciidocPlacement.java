package de.jcup.asp.api.asciidoc;

public enum AsciidocPlacement {

    TOP("top"), 
    
    BOTTOM("bottom"), 
    
    LEFT("left"), 
    
    RIGHT("right"),
    
    PREAMBLE("preamble"), 
    
    MACRO("macro");

    private String position;
    
    AsciidocPlacement(String position) {
        this.position = position;
    }
    
    public String getPosition() {
        return position;
    }
    
}
