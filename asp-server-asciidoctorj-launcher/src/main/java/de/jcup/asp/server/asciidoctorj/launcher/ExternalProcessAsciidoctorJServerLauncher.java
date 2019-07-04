package de.jcup.asp.server.asciidoctorj.launcher;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.jcup.asp.core.ASPLauncher;
import de.jcup.asp.core.CoreConstants;
import de.jcup.asp.core.ExitCode;
import de.jcup.asp.core.LaunchException;
import de.jcup.asp.core.LogHandler;
import de.jcup.asp.core.OutputHandler;
import de.jcup.asp.core.ServerExitCodes;

/**
 * This ASP launcher will create a new java process and start ASP server from given path to jar.
 * A {@link OutputHandler} can be set to obtain error and normal output from processs.
 *
 */
public class ExternalProcessAsciidoctorJServerLauncher implements ASPLauncher {
    
    private int port;
    private String pathToJava;
    private String pathToServerJar;
    private boolean showSecretKey;

    private Process process;
    private OutputHandler outputHandler;
    private LogHandler logHandler;

    public ExternalProcessAsciidoctorJServerLauncher(String pathToServerJar, int port) {
        this(null, pathToServerJar,port);
    }

    public ExternalProcessAsciidoctorJServerLauncher(String pathTojava, String pathToServerJar,int port) {
        this.pathToJava = pathTojava;
        this.pathToServerJar = pathToServerJar;
        this.port=port;
    }
    
    public void setShowSecretKey(boolean showSecretKey) {
        this.showSecretKey = showSecretKey;
    }

    /**
     * Launches an asp-server-asciidoctorj instance inside own process.
     * 
     * @return secret server key
     */
    public String launch(int timeOutInSeconds) throws LaunchException{
        ServerStartRunnable runnable = new ServerStartRunnable(port);
        Thread thread = new Thread(runnable, "ASP Launcher, port:" + port);
        thread.setDaemon(true);
        thread.start();
     
        waitForSecretKey(timeOutInSeconds, runnable);
        return runnable.secretKey;
    }

    private void waitForSecretKey(int timeOutInSeconds, ServerStartRunnable runnable) throws LaunchException {
        Duration acceptedmax = Duration.ofSeconds(timeOutInSeconds);
        Instant start = Instant.now();
        
        
        while ( hasNotEnded() && runnable.failed==null && runnable.secretKey == null) {
            try {
               Thread.sleep(500);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               break;
            }
            Instant finish = Instant.now();
            Duration duration = Duration.between(start, finish);
            if (duration.compareTo(acceptedmax)>0) {
                throw new LaunchException("Timed out after "+duration.getSeconds()+" seconds");
            }
            
        }
        if (runnable.failed!=null) {
            throw new LaunchException("Was not able to launch server on port:"+port, runnable.failed);
        }
        if (runnable.secretKey==null) {
            throw new LaunchException("Server did not return secret!",null);
        }
    }

    private boolean hasNotEnded() {
        if (process==null) {
            return true;// process has not been started
        }
        if (process.isAlive()) {
            return true;
        }
        return false;
    }

    public int getPort() {
        return port;
    }

    public void setPathToJava(String pathToJava) {
        if (Objects.equals(pathToJava, this.pathToJava)) {
            return;
        }
        this.pathToJava = pathToJava;
    }

    public void setPathToServerJar(String pathToServerJar) {
        this.pathToServerJar = pathToServerJar;
    }

    public void setOutputHandler(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    public void setLogHandler(LogHandler logHandler) {
        this.logHandler = logHandler;
    }

    /**
     * Stop server
     * 
     * @return <code>true</code> when server shutdown was successful,
     *         <code>false</code> when server was already not running
     */
    public boolean stopServer() {
        if (process == null) {
            return false;
        }
        if (!process.isAlive()) {
            return false;
        }
        process.destroyForcibly();
        return true;
    }

    private class ServerStartRunnable implements Runnable {
        private String secretKey;
        private Throwable failed;
        int port;

        private ServerStartRunnable(int port) {
            this.port = port;
        }

        public void run() {
            String javaCommand = null;
            if (pathToJava == null || pathToJava.trim().isEmpty()) {
                javaCommand = "java";
            } else {
                javaCommand = pathToJava + "/java";
                File test = new File(javaCommand);
                if (!test.exists()) {
                    if (outputHandler != null) {
                        outputHandler.output(">> Not able to start java because not found on defined location:" + javaCommand);
                    }
                    return;
                }
                if (!test.canExecute()) {
                    if (outputHandler != null) {
                        outputHandler.output(">> Not able to start java because existing but not executable: " + javaCommand);
                    }
                    return;
                }
            }

            List<String> commands = new ArrayList<String>();
            commands.add(javaCommand);
            commands.add("-Dasp.server.port=" + port);
            commands.add("-jar");
            commands.add(pathToServerJar);

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true); // we want output and error stream handled same time
            StringBuffer lineStringBuffer = new StringBuffer();
            try {
                process = pb.start();
                if (outputHandler != null) {
                    outputHandler.output(">> Triggered ASP server start at port:" + port);
                }
                try (InputStream is = process.getInputStream()) {
                    int c;
                    while ((c = is.read()) != -1) {
                        if (c == '\n') {
                            String line = lineStringBuffer.toString();
                            int secretPrefix = line.indexOf(CoreConstants.SERVER_SECRET_OUTPUT_PREFIX);
                            if (secretPrefix!=-1) {
                                secretKey=line.substring(secretPrefix+CoreConstants.SERVER_SECRET_OUTPUT_PREFIX.length());
                                if (!showSecretKey) {
                                    line = line.substring(0,secretPrefix)+CoreConstants.SERVER_SECRET_OUTPUT_PREFIX+"xxxxxxxxxxxxxxxxxxxxxxx";
                                }
                            }
                            if (outputHandler != null) {
                                outputHandler.output(line);
                            }
                            lineStringBuffer = new StringBuffer();
                        } else {
                            lineStringBuffer.append((char) c);
                        }
                    }
                }
                if (outputHandler != null) {
                    outputHandler.output(lineStringBuffer.toString());
                }
                int exitCode = process.waitFor();
                if (outputHandler != null) {
                    ExitCode exitCodeObject = ServerExitCodes.from(exitCode);
                    
                    outputHandler.output(">> ASP Server at port " + port + " exited with code:" + exitCodeObject.toMessage());
                }
            } catch (Exception e) {
                String message = ">> FATAL ASP server connection failure :" + e.getMessage();
                if (outputHandler != null) {
                    outputHandler.output(message);
                } else {
                    System.err.println(message);
                }
                if (logHandler != null) {
                    logHandler.error(message, e);
                } else {
                    e.printStackTrace();
                }
            }
        }
    }


}
