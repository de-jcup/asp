package de.jcup.asp.api;

public class Request {

    public static final String TERMINATOR = "--REQUEST-TERMINATE--";
    private ProtocolData data = new ProtocolData();

    public void set(RequestParameterKey key, ValueProvider provider) {
        String data = null;
        if (provider != null) {
            data = provider.getValue();
        }
        set(key, data);
    }

    public void set(RequestParameterKey key, String value) {
        data.data.put(key.getId(), value);
    }

    public String get(RequestParameterKey key) {
        return data.data.get(key.getId());
    }

    public String convertToString() {
        return data.convertToString();
    }

    public static Request convertFromString(String data) {
        Request r = new Request();
        r.data = ProtocolData.convertFromString(data);
        return r;
    }

    public Command getCommand() {
        String commandValue = get(RequestParameterKeys.COMMAND);
        return Commands.getById(commandValue);
    }
    
    public void setBackend(Backend backend) {
        set(RequestParameterKeys.BACKEND,backend.convertToString());
    }

    public Backend getBackend() {
        return Backend.convertFromString(get(RequestParameterKeys.BACKEND));
    }
}
