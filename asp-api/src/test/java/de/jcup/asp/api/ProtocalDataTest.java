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
        responseToTest.data.put("key1", "value1");
        
        /* execute + test */
        assertEquals("key1=value1",responseToTest.convertToString());
    }
    
    @Test
    public void two_key_value_converttostring_sorted_and_as_expected() {
        /* prepare */
        responseToTest.data.put("key2", "value2");
        responseToTest.data.put("key1", "value1");
        
        /* execute + test */
        assertEquals("key1=value1\nkey2=value2",responseToTest.convertToString());
    }
    
    @Test
    public void key_value_converfromstring_as_expected() {
        /* execute + test */
        responseToTest = ProtocolData.convertFromString("key1=value1");
        /* test */
        assertEquals("value1", responseToTest.data.get("key1"));
    }
    
    @Test
    public void key_value_2_converfromstring_as_expected() {
        /* execute + test */
        responseToTest = ProtocolData.convertFromString("key1=value1\nkey2=value2");
        /* test */
        assertEquals("value1", responseToTest.data.get("key1"));
        assertEquals("value2", responseToTest.data.get("key2"));
    }
    

}
