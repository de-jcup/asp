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
