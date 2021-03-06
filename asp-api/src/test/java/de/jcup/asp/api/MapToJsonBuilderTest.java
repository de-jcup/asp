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

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

public class MapToJsonBuilderTest {

    private MapToJsonBuilder toTest;

    @Before
    public void before() {
        toTest = new MapToJsonBuilder();
    }

    @Test
    public void test_map_contains_key_childmap_and_there_key_and_childchildmap() {
        /* prepare */
        Map<String, Object> map = new TreeMap<String, Object>();
        Map<String, Object> childMap = new TreeMap<String, Object>();
        Map<String, Object> childChildMap = new TreeMap<String, Object>();
        
        map.put("key1", "value1");
        map.put("key2",null); // will be forgotten
        childMap.put("childKey1", "childValue1");
        childChildMap.put("childChildKey1", "childChildValue1");
        map.put("child1",childMap);
        childMap.put("childChild1",childChildMap);
        
        /* execute */
        JsonObject result = toTest.toJSON(map);
        
        /* test */
        assertEquals("{\"child1\":{\"childChild1\":{\"childChildKey1\":\"childChildValue1\"},\"childKey1\":\"childValue1\"},\"key1\":\"value1\"}",result.toString());
    }
    
    @Test
    public void numberMapping() {
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("int", new Integer(1));
        map.put("long", new Long(2));
        map.put("float", new Float(3.3));
        map.put("double", new Double(4.4));
        
        /* execute */
        JsonObject result = toTest.toJSON(map);
        
        assertEquals("{\"double\":4.4,\"float\":3.3,\"int\":1,\"long\":2}", result.toString());
        
    }

}
