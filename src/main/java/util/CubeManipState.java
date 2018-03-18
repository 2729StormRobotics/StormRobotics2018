package util;

public enum CubeManipState {
    IN("IN"),
    OUT("OUT"),
    CLOCKWISE("CLOCKWISE"),
    COUNTERCLOCKWISE("COUNTERCLOCKWISE"),
    IDLE("IDLE");

    private final String state;

    CubeManipState(String state) { this.state = state; }

    public String getState() { return state; }
}
