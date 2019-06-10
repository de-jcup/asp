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
