/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
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
