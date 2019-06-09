package de.jcup.asp.api;

import java.io.File;

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
        data.jsonObject.add(key.getId(), value);
    }

    public String get(RequestParameterKey key) {
        return data.jsonObject.getString(key.getId(), "");
    }

    public String convertToString() {
        return data.convertToString();
    }

    public static Request convertFromString(String data) throws ProtocolDataException {
        Request r = new Request();
        r.data = ProtocolData.convertFromString(data);
        return r;
    }

    public Command getCommand() {
        String commandValue = get(RequestParameterKeys.COMMAND);
        return Commands.getById(commandValue);
    }

    public void setBackend(Backend backend) {
        set(RequestParameterKeys.BACKEND, backend.convertToString());
    }

    public Backend getBackend() {
        return Backend.convertFromString(get(RequestParameterKeys.BACKEND));
    }

    public void setBaseDir(File file) {
        String path = null;
        if (file != null) {
            path = file.getAbsolutePath();
        }
        set(RequestParameterKeys.BASE_DIR, path);
    }

    public File getBaseDir() {
        String path = get(RequestParameterKeys.BASE_DIR);
        if (path == null) {
            return null;
        }
        return new File(path);
    }

}
