package de.jcup.asp.api;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class JsonToMapBuilderTest {

    private JsonToMapBuilder toTest;

    @Before
    public void before() {
        toTest = new JsonToMapBuilder();
    }

    @Test
    public void test() {
        /* prepare */
        String json = "{\"child1\":{\"childChild1\":{\"childChildKey1\":\"childChildValue1\"},\"childKey1\":\"childValue1\"},\"key1\":\"value1\"}";
        JsonValue jsonValue = Json.parse(json);

        /* execute */
        Map<String, Object> result = toTest.toMap(jsonValue);

        /* test */
        Map<String, Object> expectedMap = new TreeMap<String, Object>();
        Map<String, Object> childMap = new TreeMap<String, Object>();
        Map<String, Object> childChildMap = new TreeMap<String, Object>();

        expectedMap.put("key1", "value1");
        childMap.put("childKey1", "childValue1");
        childChildMap.put("childChildKey1", "childChildValue1");
        expectedMap.put("child1", childMap);
        childMap.put("childChild1", childChildMap);

        assertEquals(expectedMap, result);

    }

    @Test
    public void numberformatting() {
        String json = "{\"double\":4.4,\"float\":3.3,\"int\":1,\"long\":2}";
        JsonValue jsonValue = Json.parse(json);

        /* execute */
        Map<String, Object> result = toTest.toMap(jsonValue);

        assertEquals(new Integer(1), result.get("int"));
        /* small long values are always reduced to integer*/
        assertEquals(new Integer(2), result.get("long"));
        
        assertEquals(new Float(3.3), result.get("float"));
        /* small double values are always reduced to float*/
        assertEquals(new Float(4.4), result.get("double"));
    }

}
