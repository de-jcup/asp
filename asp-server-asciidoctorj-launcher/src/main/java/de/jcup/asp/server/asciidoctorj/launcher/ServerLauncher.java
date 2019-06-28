package de.jcup.asp.server.asciidoctorj.launcher;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.jcup.asp.core.CoreConstants;
import de.jcup.asp.core.LogHandler;
import de.jcup.asp.core.OutputHandler;

public class ServerLauncher {
    private int port;
    private String pathToJava;
    private String pathToServerJar;

    private Process process;
    private OutputHandler outputHandler;
    private LogHandler logHandler;

    public ServerLauncher(String pathToServerJar, int port) {
        this(null, pathToServerJar,port);
    }

    public ServerLauncher(String pathTojava, String pathToServerJar,int port) {
        this.pathToJava = pathTojava;
        this.pathToServerJar = pathToServerJar;
        this.port=port;
    }

    /**
     * Launches an asp-server-asciidoctorj instance inside own process.
     * 
     * @return secret server key
     */
    public String launch() throws LaunchException{
        ServerStartRunnable runnable = new ServerStartRunnable(port);
        Thread thread = new Thread(runnable, "ASP Launcher, port:" + port);
        thread.setDaemon(true);
        thread.start();
        while (runnable.failed==null && runnable.secretKey == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               break;
            }
        }
        if (runnable.failed!=null) {
            throw new LaunchException("Was not able to launch server on port:"+port, runnable.failed);
        }
        if (runnable.secretKey==null) {
            throw new LaunchException("Server did not return secret!",null);
        }
        return runnable.secretKey;
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
                    outputHandler.output(">> Former running ASP Server at port " + port + " stopped, exit code was:" + exitCode);
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
