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

import java.util.ArrayList;
import java.util.List;

public enum Commands implements Command{
    
    CONVERT_FILE(StringRequestParameterKey.SOURCE_FILEPATH,MapRequestParameterKey.OPTIONS),
    
    RESOLVE_ATTRIBUTES_FROM_DIRECTORY(),
    
    IS_ALIVE,
    
    ;
    private String id;
    private List<RequestParameterKey<?>> requiredParamters = new ArrayList<>();
    
    private Commands(RequestParameterKey<?>... requiredParameters) {
        this.id=name().toLowerCase();
        for (RequestParameterKey<?> requird: requiredParameters) {
            if (requird!=null) {
                this.requiredParamters.add(requird);
            }
        }
    }
    
    @Override
    public String getId() {
        return id;
    }

    public static Command getById(String commandId) {
        if (commandId==null) {
            return null;
        }
        for (Command command: values()) {
            if (command.getId().equals(commandId)) {
                return command;
            }
        }
        return null;
    }
    
    @Override
    public List<RequestParameterKey<?>> getRequiredParameters() {
        return requiredParamters;
    }
}
