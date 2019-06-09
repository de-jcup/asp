package de.jcup.asp.api;

public class Response {
    public static final String TERMINATOR="--RESPONSE-TERMINATE--";
    private ProtocolData data = new ProtocolData();

    public void set(ResponseResultKey key, ValueProvider provider) {
        String data = null;
        if (provider != null) {
            data = provider.getValue();
        }
        set(key, data);
    }

    public void set(ResponseResultKey key, String value) {
        data.jsonObject.add(key.getId(), value);
    }

    public String get(ResponseResultKey key) {
        return data.jsonObject.getString(key.getId(),"");
    }

    public String convertToString() {
        return data.convertToString();
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
        return get(ResponseResultKeys.ERROR);
    }
    
    public boolean failed() {
        String error = get(ResponseResultKeys.ERROR);
        if (error==null || error.isEmpty()) {
            return false;
        }
        return true;
    }

    public void setErrorMessage(String message) {
        set(ResponseResultKeys.ERROR,message);
    }
}
