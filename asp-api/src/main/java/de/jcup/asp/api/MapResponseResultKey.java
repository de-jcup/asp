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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public enum MapResponseResultKey implements ResponseResultKey<Map<String,Object>>{
    
    RESULT_ATTRIBUTES,
    
    ;
    private static Map<String,Object> PROTOTYPE = Collections.unmodifiableMap(new TreeMap<String, Object>());
    private String id;

    private MapResponseResultKey() {
        this.id=name().toLowerCase();
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String,Object> getPrototype() {
        return PROTOTYPE;
    }
}
