package de.jcup.asp.client;

public interface AspClientCommunicationListener {

    public void sending(String unencryptedRequestString);

    public void receiving(String fromServer);
}
