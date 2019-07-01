package de.jcup.asp.client;

public class DefaultAspClientProgressMonitor implements AspClientProgressMonitor{

    private boolean canceled;

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
    
    @Override
    public boolean isCanceled() {
        return canceled;
    }

}
