package de.jcup.asp.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class ServerLog {
    
    private List<ServerLogEntry> entries = new ArrayList<>();

    public void add(ServerLogEntry entry) {
        this.entries.add(entry);
    }
    
    
    public List<ServerLogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
    
    public String convertToString() {
        JsonArray array = Json.array();
        for (ServerLogEntry entry: entries) {
            array.add(entry.convertToString());
        }
        
        return array.toString();
    }
    
    public static ServerLog convertFromString(String string) {
        ServerLog result = new ServerLog();
        if (string==null || string.isEmpty()) {
           return result;
        }
        
        JsonValue parsed = Json.parse(string);
        JsonArray array = parsed.asArray();
        
        for (JsonValue value : array) {
            String log = value.asString();
            result.add(ServerLogEntry.convertFromString(log));
        }
                
        return result;
    }

}
