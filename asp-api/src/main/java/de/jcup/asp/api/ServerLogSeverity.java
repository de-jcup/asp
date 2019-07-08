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

public enum ServerLogSeverity {

    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL,
    UNKNOWN,;
    
    /**
     * Resolves sever log severity. If not possible results in {@link ServerLogSeverity#UNKNOWN}
     * @param string
     * @return server log severity for given string, never <code>null</code>
     */
    public static ServerLogSeverity fromString(String string) {
        if (string==null) {
            return UNKNOWN;
        }
        
        for (ServerLogSeverity severity: values()) {
            if (severity.name().equalsIgnoreCase(string)) {
                return severity;
            }
        }
        return UNKNOWN;
    }
}
