package util;

public enum DebugLevel {
    INFO("Info"),
    DEBUG("Debug"),
    ALL("All");

    private final String name;

    DebugLevel(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
