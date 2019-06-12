package de.jcup.asp.api;

public enum ServerLogSeverity {

    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL,
    UNKNOWN,;
    
    /**
     * Resolves sever log severity. If not possible results in {@link ServerLogSeverity#UNKNOWN}
     * @param string
     * @return server log severity for given string, never <code>null</code>
     */
    public static ServerLogSeverity fromString(String string) {
        if (string==null) {
            return UNKNOWN;
        }
        
        for (ServerLogSeverity severity: values()) {
            if (severity.name().equalsIgnoreCase(string)) {
                return severity;
            }
        }
        return UNKNOWN;
    }
}
