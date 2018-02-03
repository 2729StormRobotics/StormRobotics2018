package util;

public enum AutoPosition {
    LEFT("Left"),
    MIDDLE("Middle"),
    RIGHT("Right");

    private final String name;

    AutoPosition(String name) { this.name = name; }

    public String getName() {
        return name;
    }
}
