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
        if (Severity.DEBUG.equals(logRecord.getSeverity())){
            /* we ignore debug statements */
            return;
        }
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
