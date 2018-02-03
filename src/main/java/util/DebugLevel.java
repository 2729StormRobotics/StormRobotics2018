package util;

public enum DebugLevel {
    INFO("Info"),
    DEBUG("Debug"),
    TRACE("Trace");

    private final String name;

    DebugLevel(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
