package de.jcup.asp.api;

public enum StringRequestParameterKey implements RequestParameterKey<String> {
     /**
      * Protocol version
      */
     VERSION,
     
     /**
      * COMMAND to execute
      */
     COMMAND,

     /**
      * Path to a FILE which shall be handled by server 
      */
     SOURCE_FILEPATH,
     
     /**
      * Path to a FOLDER where server shall do an action 
      */
     BASE_DIR,

     ;
    private String id;

    private StringRequestParameterKey() {
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
