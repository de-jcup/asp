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

public enum StringRequestParameterKey implements RequestParameterKey<String> {
     /**
      * Protocol version
      */
     VERSION,
     
     /**
      * COMMAND to execute
      */
     COMMAND,

     /**
      * Path to a FILE which shall be handled by server 
      */
     SOURCE_FILEPATH,
     
     /**
      * Path to a FOLDER where server shall do an action 
      */
     BASE_DIR,

     ;
    private String id;

    private StringRequestParameterKey() {
        this.id=name().toLowerCase();
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPrototype() {
        return "protype";
    }
    
}
