package de.jcup.asp.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AspClientProgressMonitorSurveillance {

    public static AspClientProgressMonitorSurveillance INSTANCE = new AspClientProgressMonitorSurveillance();

    private static final Logger LOG = LoggerFactory.getLogger(AspClientProgressMonitorSurveillance.class);

    private Thread surveillanceThread;
    private QueueRunnable queueRunnable;
    private Set<AspClientAction> aspClientActions = new LinkedHashSet<AspClientAction>();

    private AspClientProgressMonitorSurveillance() {
        ensureRunningClientWorkerThread();
    }

    public void ensureRunningClientWorkerThread() {
        if (surveillanceThread != null && surveillanceThread.isAlive()) {
            return;
        }
        queueRunnable = new QueueRunnable();
        surveillanceThread = new Thread(queueRunnable, "ASP Client ProgressMonitor");
        surveillanceThread.setDaemon(true);
        surveillanceThread.start();
    }

    public void cancel(AspClientAction aspClientAction) {
        try {
            aspClientAction.cancel();
        } catch (IOException e) {
            LOG.warn("Cancel operation problematic", e);
        }
    }

    public void inspect(AspClientAction clientCall, int timeOutInMilliseconds) {
        Objects.requireNonNull(clientCall);
        synchronized (aspClientActions) {
            aspClientActions.add(clientCall);
        }
    }

    public class QueueRunnable implements Runnable {
        AspClientAction current;

        @Override
        public void run() {
            List<AspClientAction> callsToRemove = new ArrayList<AspClientAction>();
            while (true) {
                synchronized (aspClientActions) {
                    try {
                        for (AspClientAction call : aspClientActions) {
                            if (call.getProgressMonitor().isCanceled()) {
                                callsToRemove.add(call);
                                call.cancel();
                            } else if (call.done()) {
                                callsToRemove.add(call);
                            }
                        }
                        aspClientActions.removeAll(callsToRemove);
                    } catch (Exception e) {
                        LOG.error("Progress monitor surveillance failure", e);
                    }

                }
                waitShortTime();

            }

        }

        private void waitShortTime() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

}