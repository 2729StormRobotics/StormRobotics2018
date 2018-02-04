package util;

public enum RobotState {
    DRIVE("Drive"),
    PTO("PTO");

    private final String state;

    RobotState(String state) { this.state = state; }

    public String getState() { return state; }
}

