package de.jcup.asp.server.asciidoctorj;

import de.jcup.asp.core.MetaInfVersionLoader;

public class Version {

    private static String version;

    static {
        loadVersion();
    }

    private static void loadVersion() {
        version = MetaInfVersionLoader.loadVersionFromMetaInf("AspServer-Version");
    }
    
    public static String getVersion() {
        return version;
    }
    
    
}
