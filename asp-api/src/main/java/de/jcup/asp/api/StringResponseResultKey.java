package de.jcup.asp.api;

public enum StringResponseResultKey implements ResponseResultKey<String>{
    VERSION,
    
    ERROR,

    RESULT_FILEPATH, 
    
    SERVER_LOGS,
    
    ;

    private String id;

    private StringResponseResultKey() {
        this.id=name().toLowerCase();
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPrototype() {
        return "protype";
    }
}
