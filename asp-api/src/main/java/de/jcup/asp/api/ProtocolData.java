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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

class ProtocolData {

    private static final Logger LOG = LoggerFactory.getLogger(ProtocolData.class);
    
    protected JsonObject jsonObject = new JsonObject(); // initial object
   
    ProtocolData() {
    }
    
    static ProtocolData convertFromString(String data) throws ProtocolDataException {
           LOG.trace("convert from String:{}",data);
           
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