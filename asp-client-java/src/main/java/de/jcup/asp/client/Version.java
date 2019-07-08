package de.jcup.asp.client;

import de.jcup.asp.core.MetaInfVersionLoader;

public class Version {

    private static String version;

    static {
        loadVersion();
    }

    private static void loadVersion() {
        version = MetaInfVersionLoader.loadVersionFromMetaInf("AspClient-Version");
    }
    
    public static String getVersion() {
        return version;
    }
    
    
}
