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

import org.junit.Before;
import org.junit.Test;

public class ServerLogTest {

    private ServerLog toTest;

    @Before
    public void before() {
        toTest = new ServerLog();
    }

    @Test
    public void error_message_with_given_file_and_linenumber_can_be_converted_and_converted_back_and_entries_are_equal() {
        ServerLogEntry loggedEntry = new ServerLogEntry(ServerLogSeverity.ERROR,"my message",new File("."),1);
        /* prepare */
        toTest.add(loggedEntry);
        
        /* execute */
        String result = toTest.convertToString();
        
        /* execute 2 - just reverse */
        ServerLog data = ServerLog.convertFromString(result);
        
        /* test */
        assertEquals(1,data.getEntries().size());
        ServerLogEntry fetchedEntry = data.getEntries().iterator().next();
        assertEquals(loggedEntry,fetchedEntry);
        
    }
    
    @Test
    public void null_values_and_n1_can_be_converted_and_converted_back_and_entries_are_equal() {
        ServerLogEntry loggedEntry = new ServerLogEntry(null,null,null,-1);
        /* prepare */
        toTest.add(loggedEntry);
        
        /* execute */
        String result = toTest.convertToString();
        
        /* execute 2 - just reverse */
        ServerLog data = ServerLog.convertFromString(result);
        
        /* test */
        assertEquals(1,data.getEntries().size());
        ServerLogEntry fetchedEntry = data.getEntries().iterator().next();
        assertEquals(loggedEntry,fetchedEntry);
        
    }
    
    @Test
    public void an_empty_log_will_contain_no_entries_when_converting() {
        
        /* execute */
        String result = toTest.convertToString();
        
        /* execute 2 - just reverse */
        ServerLog data = ServerLog.convertFromString(result);
        
        /* test */
        assertEquals(0,data.getEntries().size());
        
    }
    
    @Test
    public void an_empty_string_log_will_contain_no_entries_when_converting() {
        
        
        /* execute */
        ServerLog data = ServerLog.convertFromString("");
        
        /* test */
        assertEquals(0,data.getEntries().size());
        
    }
    
    @Test
    public void an_null_string_log_will_contain_no_entries_when_converting() {
        
        
        /* execute */
        ServerLog data = ServerLog.convertFromString(null);
        
        /* test */
        assertEquals(0,data.getEntries().size());
        
    }

}
