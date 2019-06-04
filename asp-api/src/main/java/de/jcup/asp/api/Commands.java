package de.jcup.asp.api;

public enum Commands implements Command{
    CONVERT_LOCALFILE
    ;
    private String id;

    private Commands() {
        this.id=name().toLowerCase();
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
}
