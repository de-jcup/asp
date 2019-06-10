package de.jcup.asp.api;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public enum MapResponseResultKey implements ResponseResultKey<Map<String,Object>>{
    
    RESULT_ATTRIBUTES,
    
    ;
    private static Map<String,Object> PROTOTYPE = Collections.unmodifiableMap(new TreeMap<String, Object>());
    private String id;

    private MapResponseResultKey() {
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
