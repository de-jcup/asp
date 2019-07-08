/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.asp.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Response extends AbstractProtocolObject {

    public static final String TERMINATOR = "--RESPONSE-TERMINATE--";

    public <T> void set(ResponseResultKey<T> key, ValueProvider<T> provider) {
        internalSet(key, provider);
    }

    public void set(ResponseResultKey<String> key, String value) {
        internalSet(key, value);
    }

    public void setServerLog(ServerLog log) {
        internalSet(StringResponseResultKey.SERVER_LOGS, log.convertToString());
    }
    
    public ServerLog getServerLog() {
        String json = internalGetString(StringResponseResultKey.SERVER_LOGS);
        return ServerLog.convertFromString(json);
    }
    
    public Path getResultFilePath() {
        String filePath = getString(StringResponseResultKey.RESULT_FILEPATH);
        if (filePath==null) {
            return null;
        }
        Path path = Paths.get(filePath);
        return path;
    }
    
    public Map<String,Object> getAttributes(){
        return getMap(MapResponseResultKey.RESULT_ATTRIBUTES);
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
