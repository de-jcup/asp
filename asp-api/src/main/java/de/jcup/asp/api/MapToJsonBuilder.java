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

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

class MapToJsonBuilder {

    public JsonObject toJSON(Map<String, Object> map) {
        JsonObject jsonObj = new JsonObject();
        for (String key : map.keySet()) {
            Object value = resolveKeyOrNull(map, key);
            if (value instanceof JsonValue) {
                JsonValue jsonValue = (JsonValue) value;
                jsonObj.add(key, jsonValue);
            }else if (value instanceof String) {
                String str = (String) value;
                jsonObj.add(key, str);
            }else if (value instanceof Boolean){
                Boolean bool = (Boolean)value;
                jsonObj.add(key, bool);
            }else if (value instanceof Float) {
                Float flt = (Float) value;
                jsonObj.add(key, flt);
            }else if (value instanceof Double) {
                Double dbl = (Double) value;
                jsonObj.add(key, dbl);
            }else if (value instanceof Long) {
                Long lng = (Long) value;
                jsonObj.add(key, lng);
            }else if (value instanceof Integer) {
                Integer integer = (Integer) value;
                jsonObj.add(key, integer);
            }
        }
        return jsonObj;
    }
    
    private Object resolveKeyOrNull(Map<String, Object> map, String key) {
        Object raw = map.get(key);
        if (raw instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map2 = (Map<String, Object>) raw;
            return toJSON(map2);
        }
        return raw;
    }

}
