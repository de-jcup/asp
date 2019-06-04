package de.jcup.asp.api;

public enum Backend {

        HTML,
        
        PDF,
        
        ;
    
    private static final Backend DEFAULT = HTML;
    
    private Backend() {
        
    }
    
    public String convertToString() {
        return name().toLowerCase();
    }

    public String getFileNameEnding() {
        return name().toLowerCase();
    }
    
    public static Backend convertFromString(String string) {
        if (string==null) {
            return DEFAULT;
        }
        for (Backend b : Backend.values()) {
            if (b.name().equalsIgnoreCase(string)) {
                return b;
            }
        }
        return DEFAULT;
    }
}
