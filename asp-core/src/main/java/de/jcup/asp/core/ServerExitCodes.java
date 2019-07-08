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
package de.jcup.asp.core;

public enum ServerExitCodes implements ExitCode{
    OK(0,"OK"),
    
    ERROR_PORT_ALREADY_USED(10,"Port already in use!"),
    
    ERROR(1,"Some common error happened"),
    ;
    private int exitCode;
    private String description;
    
    private ServerExitCodes(int exitCode, String description) {
        this.exitCode=exitCode;
        this.description=description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getExitCode() {
        return exitCode;
    }

    public static ExitCode from(int exitCode) {
        for (ExitCode code: ServerExitCodes.values()) {
            if (code.getExitCode()==exitCode) {
                return code;
            }
        }
        return new UnknownExitCode(exitCode);
    }
}
