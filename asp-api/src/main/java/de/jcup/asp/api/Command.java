package de.jcup.asp.api;

public interface Command extends Identifiable, ValueProvider{

    @Override
    default String getValue() {
        return getId();
    }
}
