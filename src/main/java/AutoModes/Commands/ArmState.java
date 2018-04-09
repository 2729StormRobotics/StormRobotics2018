package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

public class ArmState extends Command {

    boolean armCountry, finish;

    public ArmState(boolean up) {
        armCountry = up;
    }

    public synchronized void start() {
        super.start();
    }
    protected void end() {
        super.end();
    }

    protected void initialize() {
        super.initialize();
    }

    protected void execute() {
        Robot._intake.setIntakeArm(armCountry);
        finish = true;
    }

    protected boolean isFinished() {
        return finish;
    }

}
