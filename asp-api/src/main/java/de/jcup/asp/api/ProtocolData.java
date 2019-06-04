package de.jcup.asp.api;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolData {

    private static final Logger LOG = LoggerFactory.getLogger(ProtocolData.class);
    protected Map<String, String> data = new TreeMap<>();
    ProtocolData() {
    }
    static ProtocolData convertFromString(String data) {
           ProtocolData r = new ProtocolData();
           if (data==null) {
               return r;
           }
           String trimmed = data.trim();
           if (trimmed.isEmpty()) {
               return r;
           }
           String[] lines = trimmed.split("\n");
           if (lines.length==0) {
               return r;
           }
           for (String line: lines) {
               if (line==null) {
                   continue;
               }
               LOG.trace(line);
               String[] splitted = line.trim().split("=");
               if (splitted.length!=2) {
                   LOG.error("Split result:{}, so line is not key=value syntax:{}",splitted.length, line);
               }else {
                   r.data.put(splitted[0], splitted[1]);
               }
           }
           return r;
       }

    String convertToString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = data.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            sb.append(key);
            sb.append('=');
            sb.append(data.get(key));
    
            if (it.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

   

}