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

import java.io.File;

import org.junit.Test;

public class ServerLogEntryTest {


    @Test
    public void equals_when_same_values() {
        /* prepare */
        ServerLogEntry loggedEntry1 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",new File("."),1);
        ServerLogEntry loggedEntry2 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",new File("."),1);
        
        /* test */
        assertEquals(loggedEntry1,loggedEntry2);
        
    }
    
    @Test
    public void not_equals_when_different_severity() {
        /* prepare */
        ServerLogEntry loggedEntry1 = new ServerLogEntry(ServerLogSeverity.INFO,"my message",new File("."),1);
        ServerLogEntry loggedEntry2 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",new File("."),1);
        
        /* test */
        assertNotEquals(loggedEntry1,loggedEntry2);
        
    }
    
    @Test
    public void not_equals_when_different_message() {
        /* prepare */
        ServerLogEntry loggedEntry1 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message1",new File("."),1);
        ServerLogEntry loggedEntry2 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message2",new File("."),1);
        
        /* test */
        assertNotEquals(loggedEntry1,loggedEntry2);
        
    }
    
    @Test
    public void not_equals_when_different_files() {
        /* prepare */
        ServerLogEntry loggedEntry1 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",null,1);
        ServerLogEntry loggedEntry2 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",new File("."),1);
        
        /* test */
        assertNotEquals(loggedEntry1,loggedEntry2);
        
    }
    
    @Test
    public void not_equals_when_different_line_numbers() {
        /* prepare */
        ServerLogEntry loggedEntry1 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",new File("."),1);
        ServerLogEntry loggedEntry2 = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",new File("."),2);
        
        /* test */
        assertNotEquals(loggedEntry1,loggedEntry2);
        
    }
    
    @Test
    public void convert_from_string_empty() {
        /* execute */
        ServerLogEntry converted = ServerLogEntry.convertFromString("  ");
        
        /* test */
        assertNotNull(converted);
        
    }
    
    @Test
    public void convert_from_string_null() {
        /* execute */
        ServerLogEntry converted = ServerLogEntry.convertFromString(null);
        
        /* test */
        assertNotNull(converted);
        
    }

}
