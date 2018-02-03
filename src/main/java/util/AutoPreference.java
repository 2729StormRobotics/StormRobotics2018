package util;

public enum AutoPreference {
    SCALE("Scale"),
    SWITCH("Switch");

    private final String name;

    AutoPreference(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
