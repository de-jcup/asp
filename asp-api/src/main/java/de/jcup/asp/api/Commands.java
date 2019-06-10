package de.jcup.asp.api;

import java.util.ArrayList;
import java.util.List;

public enum Commands implements Command{
    
    CONVERT_FILE(StringRequestParameterKey.SOURCE_FILEPATH,MapRequestParameterKey.OPTIONS),
    
    RESOLVE_ATTRIBUTES_FROM_DIRECTORY(),
    
    ;
    private String id;
    private List<RequestParameterKey<?>> requiredParamters = new ArrayList<>();
    
    private Commands(RequestParameterKey<?>... requiredParameters) {
        this.id=name().toLowerCase();
        for (RequestParameterKey<?> requird: requiredParameters) {
            if (requird!=null) {
                this.requiredParamters.add(requird);
            }
        }
    }
    
    @Override
    public String getId() {
        return id;
    }

    public static Command getById(String commandId) {
        if (commandId==null) {
            return null;
        }
        for (Command command: values()) {
            if (command.getId().equals(commandId)) {
                return command;
            }
        }
        return null;
    }
    
    @Override
    public List<RequestParameterKey<?>> getRequiredParameters() {
        return requiredParamters;
    }
}
