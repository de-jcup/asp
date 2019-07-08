package de.jcup.asp.core;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

public class MetaInfVersionLoader {

    public static String loadVersionFromMetaInf(String key) {
        Enumeration<URL> resEnum;
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            resEnum = contextClassLoader.getResources(JarFile.MANIFEST_NAME);
            while (resEnum.hasMoreElements()) {
                try {
                    URL url = (URL) resEnum.nextElement();
                    InputStream is = url.openStream();
                    if (is != null) {
                        Manifest manifest = new Manifest(is);
                        
                        Attributes mainAttribs = manifest.getMainAttributes();
                        String versionAsString = mainAttribs.getValue(key);
                        
                        if (versionAsString != null) {
                            return versionAsString;
                        }
                    }
                } catch (Exception e) {
                    // Silently ignore
                }
            }
        } catch (IOException e1) {
            // Silently ignore
        }
        return "0.0.0";
    }
}
