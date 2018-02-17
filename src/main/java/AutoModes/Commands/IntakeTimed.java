package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

public class IntakeTimed extends Command {

    long startTime, addedTime;

    public IntakeTimed(long seconds) {
        startTime = System.currentTimeMillis();
        addedTime = seconds;
    }

    @Override
    protected void execute() {
        super.execute();
        Robot._elevator.toggleOutput();
    }

    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis() == startTime + addedTime) {
            Robot._elevator.toggleOutput();
            return true;
        }
        return false;
    }
}
