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

import java.util.Map;
import java.util.TreeMap;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonObject.Member;

public class JsonToMapBuilder {

    public Map<String, Object> toMap(JsonValue jsonValue) {
        Map<String, Object> map = new TreeMap<String, Object>();
        if (!jsonValue.isObject()) {
            return map;
        }
        JsonObject jsonObject = jsonValue.asObject();

        for (Member member : jsonObject) {
            String name = member.getName();
            JsonValue value = member.getValue();
            if (value.isObject()) {
                map.put(name, toMap(value));
            } else {
                if (value.isBoolean()) {
                    map.put(name, value.asBoolean());
                }else if (value.isString()) {
                    map.put(name, value.asString());
                }else if (value.isNull()) {
                    continue;
                }else if (value.isNumber()) {
                    boolean resolved= false;
                    // first matching variant will be used 
                    // unfortunately no other way to get the number format
                    resolved = resolved || tryToResolveAsInteger(name, value, map);
                    resolved = resolved || tryToResolveAsLong(name, value, map);
                    resolved = resolved || tryToResolveAsFloat(name, value, map);
                    resolved = resolved || tryToResolveAsDouble(name, value, map);
                }
            }
        }
        return map;
    }

    
    private boolean tryToResolveAsFloat(String name, JsonValue value, Map<String, Object> map) {
        try {
            map.put(name, value.asFloat());
            return true;
        }catch (RuntimeException e) {
            return false;
        }
    }
    private boolean tryToResolveAsDouble(String name, JsonValue value, Map<String, Object> map) {
       try {
           map.put(name, value.asDouble());
           return true;
       }catch (RuntimeException e) {
           return false;
       }
    }
    private boolean tryToResolveAsInteger(String name, JsonValue value, Map<String, Object> map) {
        try {
            map.put(name, value.asInt());
            return true;
        }catch (RuntimeException e) {
            return false;
        }
     }
    private boolean tryToResolveAsLong(String name, JsonValue value, Map<String, Object> map) {
        try {
            map.put(name, value.asInt());
            return true;
        }catch (RuntimeException e) {
            return false;
        }
     }
    
}
