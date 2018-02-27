package util;

public enum CubeManipState {
    IN("IN"),
    OUT("OUT"),
    IDLE("IDLE");

    private final String state;

    CubeManipState(String state) { this.state = state; }

    public String getState() { return state; }
}
