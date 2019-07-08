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
import java.util.Objects;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ServerLogEntry{
    
    private ServerLogSeverity severity;
    private String message;
    private File file;
    private int lineNumber;

    public ServerLogEntry(ServerLogSeverity severity, String message, File file, int lineNumber) {
        if (severity==null) {
            severity = ServerLogSeverity.UNKNOWN;
        }
        this.severity=severity;
        this.message=message;
        if (file!=null) {
            /* avoid problems on equals, because we use absolute path to convert back */
            file = file.getAbsoluteFile();
        }
        this.file=file;
        this.lineNumber=lineNumber;
    }
    
    public ServerLogSeverity getSeverity() {
        return severity;
    }
    
    public String getMessage() {
        return message;
    }
    
    public File getFile() {
        return file;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, lineNumber, message, severity);
    }

    public String convertToString() {
        JsonObject obj = Json.object();
        obj.add("severity", severity.name());
        if (file!=null) {
            obj.add("file", file.getAbsolutePath());
        }
        if (message!=null) {
            obj.add("message", message);
        }
        obj.add("line", lineNumber);
        return obj.toString();
    }
    
    public static ServerLogEntry convertFromString(String string) {
        if (string==null || string.trim().isEmpty()) {
            string="{}";
        }
        JsonValue data = Json.parse(string);
        JsonObject obj = data.asObject();
        JsonValue filePath = obj.get("file");
        File file = null;
        if (filePath!=null) {
            file = new File(filePath.asString());
        }
        int lineNumber = -1;
        JsonValue line = obj.get("line");
        
        if (line!=null) {
            lineNumber = line.asInt();
        }
        ServerLogSeverity severity = null;
        JsonValue sever = obj.get("severity");
        if (sever!=null) {
            String severityAsString = sever.asString();
            severity = ServerLogSeverity.fromString(severityAsString);
        }
        String message = null;
        JsonValue msg = obj.get("message");
        if (msg!=null) {
            message = msg.asString();
        }
        
        ServerLogEntry entry = new ServerLogEntry(severity, message, file, lineNumber);
        return entry;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ServerLogEntry)) {
            return false;
        }
        ServerLogEntry other = (ServerLogEntry) obj;
        return Objects.equals(file, other.file) && lineNumber == other.lineNumber && Objects.equals(message, other.message) && severity == other.severity;
    }

    @Override
    public String toString() {
        return "ServerLogEntry [severity=" + severity + ", message=" + message + ", file=" + file + ", lineNumber=" + lineNumber + "]";
    }
    
}