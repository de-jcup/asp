package de.jcup.asp.api;

public enum RequestParameterKeys implements RequestParameterKey {
     
     COMMAND,

     SOURCE_FILEPATH,
     
     BACKEND,

     ;
    private String id;

    private RequestParameterKeys() {
        this.id=name().toLowerCase();
    }
    
    @Override
    public String getId() {
        return id;
    }
    
}
