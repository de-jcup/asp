package de.jcup.asp.integrationtest;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class FullIntegrationTestRule implements TestRule {
    private static final String EXTERNAL_SERVER_TEST_ENABLED = "asp.integrationtest.full.enabled";
    private static final String PATH_TO_SERVER_JAR = "asp.integrationtest.full.pathtojar";

    @Override
    public Statement apply(Statement base, Description description) {
        return new IntegrationTestStatement(base, description);
    }

    private class IntegrationTestStatement extends Statement {
        private final Statement next;

        public IntegrationTestStatement(Statement base, Description description) {
            next = base;
        }

        @Override
        public void evaluate() throws Throwable {
            /* skip tests when not in integration test mode */
            boolean integrationTestEnabled;

            integrationTestEnabled = Boolean.getBoolean(EXTERNAL_SERVER_TEST_ENABLED);
            if (!integrationTestEnabled) {
                String message = "Skipped full integration test, to enabled define\n-D" + EXTERNAL_SERVER_TEST_ENABLED + "=true";
                Assume.assumeTrue(message, false);
            }
            next.evaluate();
        }

    }
    
    public String getEnsuredPathToServerJar() {
       String path =  System.getProperty(PATH_TO_SERVER_JAR);
       if (path==null || path.isEmpty()) {
           throw new IllegalStateException("Path to asp server jar not set! Testcase corrupt");
       }
       File file = new File(path).getAbsoluteFile();
       System.out.println(">> using server: "+file.getAbsolutePath());
       if (!file.exists()) {
           fail("File does not exist:"+file);
       }
       return path;
    }
}
