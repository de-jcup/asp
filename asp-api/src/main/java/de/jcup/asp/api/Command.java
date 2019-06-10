package de.jcup.asp.api;

import java.util.List;

public interface Command extends Identifiable, ValueProvider<String>{

    @Override
    default String getValue() {
        return getId();
    }
    
    public List<RequestParameterKey<?>> getRequiredParameters();
}
