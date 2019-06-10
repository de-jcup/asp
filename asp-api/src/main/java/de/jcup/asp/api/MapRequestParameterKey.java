package de.jcup.asp.api;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public enum MapRequestParameterKey implements RequestParameterKey<Map<String,Object>> {
     
     /**
      * Path to a FOLDER where server shall do an action 
      */
     OPTIONS,

     ;
    private String id;
    private static Map<String,Object> PROTOTYPE = Collections.unmodifiableMap(new TreeMap<String, Object>());
    
    private MapRequestParameterKey() {
        this.id=name().toLowerCase();
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String,Object> getPrototype() {
        return PROTOTYPE;
    }
    
}
