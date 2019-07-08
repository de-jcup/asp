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
package de.jcup.asp.server.asciidoctorj.provider;

import java.io.File;

import org.asciidoctor.ast.Cursor;
import org.asciidoctor.log.LogHandler;
import org.asciidoctor.log.LogRecord;
import org.asciidoctor.log.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.ServerLog;
import de.jcup.asp.api.ServerLogEntry;
import de.jcup.asp.api.ServerLogSeverity;

public class LogDataProvider implements LogHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LogDataProvider.class);

    private ServerLog serverLog = new ServerLog();
    private Object monitor = new Object();

    @Override
    public void log(LogRecord logRecord) {
        ServerLogEntry logEntry = convert(logRecord);
        
        synchronized (monitor) {
            serverLog.add(logEntry);
            LOG.debug("Added log entry {}",logEntry);
        }
    }

    /**
     * @return current log data. Will also reset log data!
     */
    public ServerLog getLogData() {
        ServerLog result;
        synchronized (monitor) {
            result = serverLog;
            serverLog = new ServerLog();
        }
        return result;
    }

    private ServerLogEntry convert(LogRecord logRecord) {
        /* severity */
        Severity logSeverity = logRecord.getSeverity();
        ServerLogSeverity severity = null;
        if (logSeverity != null) {
            severity= ServerLogSeverity.fromString(logSeverity.name());
        }
        int lineNumber = -1;
    
        Cursor cursor = logRecord.getCursor();
        if (cursor != null) {
            lineNumber = cursor.getLineNumber();
        }
        File file = null;
        if (cursor != null) {
            String filePath = cursor.getFile();
            if (filePath != null) {
                file = new File(filePath);
            }
        }
        String message = logRecord.getMessage();
        
        ServerLogEntry logEntry = new ServerLogEntry(severity, message, file, lineNumber);
        return logEntry;
    }

}
