package util;

public enum CrossPreference {
    CROSS("CROSS"),
    NOCROSS("NOCROSS");

    private final String name;

    CrossPreference(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
