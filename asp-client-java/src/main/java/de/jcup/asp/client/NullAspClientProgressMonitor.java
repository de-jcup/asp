package de.jcup.asp.client;

public class NullAspClientProgressMonitor implements AspClientProgressMonitor {

    public static final AspClientProgressMonitor NULL_PROGRESS = new NullAspClientProgressMonitor();

    private NullAspClientProgressMonitor() {
        
    }
    
    @Override
    public boolean isCanceled() {
        return false;
    }

}
