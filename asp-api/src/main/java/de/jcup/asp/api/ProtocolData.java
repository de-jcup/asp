package de.jcup.asp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ProtocolData {

    private static final Logger LOG = LoggerFactory.getLogger(ProtocolData.class);
    
    protected JsonObject jsonObject = new JsonObject(); // initial object
   
    ProtocolData() {
    }
    
    static ProtocolData convertFromString(String data) throws ProtocolDataException {
           LOG.debug("convert from String:{}",data);
           
           ProtocolData r = new ProtocolData();
           if (data==null) {
               return r;
           }
           try {
               r.jsonObject= Json.parse(data).asObject();
           }catch(RuntimeException e) {
               throw new ProtocolDataException("Was not able to convert given data",e);
           }
           
           return r;
       }

    String convertToString() {
       return jsonObject.toString();
    }

   

}