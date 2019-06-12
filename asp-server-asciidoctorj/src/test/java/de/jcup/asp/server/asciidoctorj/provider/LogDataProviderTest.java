package de.jcup.asp.server.asciidoctorj.provider;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.asciidoctor.ast.Cursor;
import org.asciidoctor.log.LogRecord;
import org.asciidoctor.log.Severity;
import org.junit.Before;
import org.junit.Test;

import de.jcup.asp.api.ServerLog;
import de.jcup.asp.api.ServerLogEntry;
import de.jcup.asp.api.ServerLogSeverity;

import static org.mockito.Mockito.*;

public class LogDataProviderTest {
    private LogDataProvider toTest;

    @Before
    public void before() {
        toTest = new LogDataProvider();
    }

    @Test
    public void test_logdata_is_cleand_after_get() {
        /* prepare */
        LogRecord log = new LogRecord(Severity.DEBUG, "message1");

        /* execute */
        toTest.log(log);

        /* test */
        ServerLog data = toTest.getLogData();
        List<ServerLogEntry> entries = data.getEntries();
        assertEquals(1, entries.size());

        data = toTest.getLogData();
        entries = data.getEntries();
        assertEquals(0, entries.size());
    }

    @Test
    public void test_conversion_debug_message1() {
        /* prepare */
        LogRecord log = new LogRecord(Severity.DEBUG, "message1");

        /* execute */
        toTest.log(log);

        /* test */
        ServerLog data = toTest.getLogData();
        List<ServerLogEntry> entries = data.getEntries();
        assertEquals(1, entries.size());

        assertEquals(new ServerLogEntry(ServerLogSeverity.DEBUG, "message1", null, -1), entries.iterator().next());
    }

    @Test
    public void test_conversion_debug_message1_file_linenumber() {
        /* prepare */
        Cursor cursor = mock(Cursor.class);
        when(cursor.getLineNumber()).thenReturn(20);
        when(cursor.getFile()).thenReturn(new File(".").getAbsolutePath());

        LogRecord log = new LogRecord(Severity.DEBUG, cursor, "message2");

        /* execute */
        toTest.log(log);

        /* test */
        ServerLog data = toTest.getLogData();

        List<ServerLogEntry> entries = data.getEntries();
        assertEquals(1, entries.size());

        assertEquals(new ServerLogEntry(ServerLogSeverity.DEBUG, "message2", new File("."), 20), entries.iterator().next());
    }

}
