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

import org.junit.Before;
import org.junit.Test;

import de.jcup.asp.api.ProtocolData;

public class ProtocalDataTest {

    private ProtocolData responseToTest;

    @Before
    public void before() {
        responseToTest = new ProtocolData();
    }
    
    @Test
    public void key_value_converttostring_as_expected() {
        /* prepare */
        responseToTest.jsonObject.add("key1", "value1");
        
        /* execute + test */
        assertEquals("{\"key1\":\"value1\"}",responseToTest.convertToString());
    }
    
    @Test
    public void key_values_are_in_same_ordering_as_inserted() {
        /* prepare */
        responseToTest.jsonObject.add("key2", "value2");
        responseToTest.jsonObject.add("key1", "value1");
        responseToTest.jsonObject.add("key3", "value3");
        responseToTest.jsonObject.add("key4", "value4");
        responseToTest.jsonObject.add("key5", "value5");
        
        /* execute + test */
        assertEquals("{\"key2\":\"value2\",\"key1\":\"value1\",\"key3\":\"value3\",\"key4\":\"value4\",\"key5\":\"value5\"}",responseToTest.convertToString());
    }
    
    @Test
    public void key_value_converfromstring_as_expected() throws Exception{
        /* execute + test */
        responseToTest = ProtocolData.convertFromString("{\"key1\":\"value1\"}");
        /* test */
        assertEquals("value1", responseToTest.jsonObject.getString("key1",null));
    }
    
    @Test
    public void key_value_2_converfromstring_as_expected() throws Exception{
        /* execute + test */
        responseToTest = ProtocolData.convertFromString("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        /* test */
        assertEquals("value1", responseToTest.jsonObject.getString("key1",null));
        assertEquals("value2", responseToTest.jsonObject.getString("key2",null));
    }
    

}
