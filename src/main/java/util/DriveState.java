package util;

public enum DriveState {
    DRIVE("Drive"),
    PTO("PTO");

    private final String state;

    DriveState(String state) { this.state = state; }

    public String getState() { return state; }
}

