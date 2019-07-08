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

import java.util.HashMap;
import java.util.Map;

public class AbstractProtocolObject {
    public static final String TERMINATOR = "--RESPONSE-TERMINATE--";
    ProtocolData data = new ProtocolData();
    JsonToMapBuilder jsonToMap = new JsonToMapBuilder();
    MapToJsonBuilder mapToJson = new MapToJsonBuilder();

    <K> void internalSet(APIKey<K> key, ValueProvider<K> provider) {
        K data = null;
        if (provider != null) {
            data = provider.getValue();
        }
        internalSet(key, data);
    }

    @SuppressWarnings("unchecked")
    <K> void internalSet(APIKey<K> key, K value) {
        if (key.getPrototype() instanceof String) {
            internalSetString(key, value.toString());
        } else if (key.getPrototype() instanceof Map) {
            internalSetMap(key, (Map<String, Object>) value);
        } else {
            throw new IllegalArgumentException("Unsupported key prototype:" + key.getPrototype());
        }
    }

    void internalSetString(APIKey<?> key, String value) {
        data.jsonObject.add(key.getId(), value);

    }

    String internalGetString(APIKey<String> key) {
        return data.jsonObject.getString(key.getId(), "");
    }

    Map<String, Object> internalGetMap(APIKey<Map<String, Object>> key) {
        if (key==null) {
            return null;
        }
        return jsonToMap.toMap(data.jsonObject.get(key.getId()));

    }

    public final String convertToString() {
        return data.convertToString();
    }

    /**
     * Sets a map into given key
     * 
     * @param mapKey
     * @param map
     */
    private void internalSetMap(APIKey<?> mapKey, Map<String, Object> map) {
        if (map==null) {
            map = new HashMap<String, Object>(0);
        }
        data.jsonObject.add(mapKey.getId(), mapToJson.toJSON(map));
    }

}
