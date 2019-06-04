package de.jcup.asp.client;

public class AspClientException extends Exception {

    private static final long serialVersionUID = 9173392130227557914L;

    public AspClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public AspClientException(String message) {
        super(message);
    }

    public AspClientException(Throwable cause) {
        super(cause);
    }


}
