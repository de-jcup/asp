package de.jcup.asp.api;

import java.util.Map;

public class Response extends AbstractProtocolObject {

    public static final String TERMINATOR = "--RESPONSE-TERMINATE--";

    public <T> void set(ResponseResultKey<T> key, ValueProvider<T> provider) {
        internalSet(key, provider);
    }

    public void set(ResponseResultKey<String> key, String value) {
        internalSet(key, value);
    }
    
    /**
     * Sets a map into given key
     * 
     * @param mapKey
     * @param map
     */
    public void set(ResponseResultKey<Map<String, Object>> mapKey, Map<String, Object> map) {
        internalSet(mapKey, map);
    }

    public String getString(ResponseResultKey<String> key) {
        return internalGetString(key);
    }
    
    public Map<String,Object> getMap(ResponseResultKey<Map<String,Object>> key) {
        return internalGetMap(key);
    }


    public static Response convertFromString(String data) {
        Response r = new Response();
        try {
            r.data = ProtocolData.convertFromString(data);
        } catch (ProtocolDataException e) {
            r.setErrorMessage(e.getMessage());
        }
        return r;
    }

    public String getErrorMessage() {
        return internalGetString(StringResponseResultKey.ERROR);
    }

    public boolean failed() {
        String error = getString(StringResponseResultKey.ERROR);
        if (error == null || error.isEmpty()) {
            return false;
        }
        return true;
    }

    public void setErrorMessage(String message) {
        set(StringResponseResultKey.ERROR, message);
    }

}
