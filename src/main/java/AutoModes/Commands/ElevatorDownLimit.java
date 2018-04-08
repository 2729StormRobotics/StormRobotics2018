package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

public class ElevatorDownLimit extends Command {
    double percentSpeed;

    public ElevatorDownLimit(double _percentSpeed) {
        percentSpeed = _percentSpeed;
    }

    @Override
    protected void initialize() {
        Robot._intake.setIntakeArm(false);
        Robot._elevator.elevate(Math.abs(percentSpeed));
        super.initialize();
    }

    @Override
    protected void execute() {
        super.execute();
        if(Robot._limitSwitch.get()) Robot._elevator.elevate(percentSpeed); else Robot._elevator.elevate(0);
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
        return !Robot._limitSwitch.get();
    }
}
