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

public enum Backend {

        HTML,
        
        PDF,
        
        ;
    
    private static final Backend DEFAULT = HTML;
    
    private Backend() {
        
    }
    
    public String convertToString() {
        return name().toLowerCase();
    }

    public String getFileNameEnding() {
        return name().toLowerCase();
    }
    
    public static Backend convertFromString(String string) {
        if (string==null) {
            return DEFAULT;
        }
        for (Backend b : Backend.values()) {
            if (b.name().equalsIgnoreCase(string)) {
                return b;
            }
        }
        return DEFAULT;
    }

    public static String getOptionId() {
        return "backend";
    }

    public static Backend getDefault() {
        return DEFAULT;
    }
}
