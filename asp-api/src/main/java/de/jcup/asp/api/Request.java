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

import java.io.File;
import java.util.Map;

public class Request extends AbstractProtocolObject{

    public static final String TERMINATOR = "--REQUEST-TERMINATE--";

    public void set(RequestParameterKey<String> key, ValueProvider<String> provider) {
        internalSet(key, provider);
    }
    public void set(RequestParameterKey<String> key, String value) {
        internalSet(key, value);
    }
    public void set(RequestParameterKey<Map<String,Object>> key, Map<String,Object> map) {
        internalSet(key, map);
    }

    public String getString(RequestParameterKey<String> key) {
        return internalGetString(key);
    }
    public Map<String,Object> getMap(RequestParameterKey<Map<String,Object>> key) {
        return internalGetMap(key);
    }

    public static Request convertFromString(String data) throws ProtocolDataException {
        Request r = new Request();
        r.data = ProtocolData.convertFromString(data);
        return r;
    }

    public Command getCommand() {
        String commandValue = getString(StringRequestParameterKey.COMMAND);
        return Commands.getById(commandValue);
    }

    public void setBaseDir(File file) {
        String path = null;
        if (file != null) {
            path = file.getAbsolutePath();
        }
        set(StringRequestParameterKey.BASE_DIR, path);
    }

    public File getBaseDir() {
        String path = getString(StringRequestParameterKey.BASE_DIR);
        if (path == null) {
            return null;
        }
        return new File(path);
    }

}
