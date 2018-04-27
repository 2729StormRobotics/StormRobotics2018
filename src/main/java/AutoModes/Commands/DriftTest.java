package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

public class DriftTest extends Command{
    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void execute() {
        Robot._driveTrain.tankDrive(0.75, 0.75, false);
        super.execute();
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
