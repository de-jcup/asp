package de.jcup.asp.integrationtest;

public class TimeAssertData{
    int seconds;
    private boolean maximum;

    private TimeAssertData(int seconds, boolean maximum){
        this.seconds=seconds;
        this.maximum=maximum;
    }
    
    public int getSeconds() {
        return seconds;
    }
    
    public boolean isMaximumAsserted() {
        return maximum && seconds>0;
    }

    public boolean isMinimumAsserted() {
        return !maximum && seconds>0;
    }
    
    public static TimeAssertData none() {
        return new TimeAssertData(0, false);
    }
    
    public static TimeAssertData min(int seconds) {
        return new TimeAssertData(seconds, false);
    }
    public static TimeAssertData max(int seconds) {
        return new TimeAssertData(seconds, true);
    }

}