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
package de.jcup.asp.integrationtest;

public class TimeAssertData{
    int seconds;
    private boolean maximum;

    private TimeAssertData(int seconds, boolean maximum){
        this.seconds=seconds;
        this.maximum=maximum;
    }
    
    public int getSeconds() {
        return seconds;
    }
    
    public boolean isMaximumAsserted() {
        return maximum && seconds>0;
    }

    public boolean isMinimumAsserted() {
        return !maximum && seconds>0;
    }
    
    public static TimeAssertData none() {
        return new TimeAssertData(0, false);
    }
    
    public static TimeAssertData min(int seconds) {
        return new TimeAssertData(seconds, false);
    }
    public static TimeAssertData max(int seconds) {
        return new TimeAssertData(seconds, true);
    }

}