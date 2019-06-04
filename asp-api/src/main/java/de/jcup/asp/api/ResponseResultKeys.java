package de.jcup.asp.api;

public enum ResponseResultKeys implements ResponseResultKey{

    ERROR,

    RESULT_FILEPATH,
    
    ;

    private String id;

    private ResponseResultKeys() {
        this.id=name().toLowerCase();
    }
    
    @Override
    public String getId() {
        return id;
    }
}
