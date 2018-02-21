package AutoModes.Commands;

import Subsystems.Elevator;
import edu.wpi.first.wpilibj.command.Command;
import robot.Constants;
import robot.Robot;

public class BangBang extends Command {

    double setPoint;

    public BangBang(double height) {
        setPoint = Elevator.checkHeight(height * Constants.ELEVATOR_TICKS_PER_INCH);
    }

    public synchronized void start() {
        super.start();
        System.err.println("start BangBang");
    }
    protected void end() {
        super.end();
    }

    /**
     * In the case the Command is interrupted turn off Elevator
     * @see Command#interrupted()
     */
    @Override
    protected void interrupted() {
        super.interrupted();
    }

    /**
     * Sets up PID controller for Elevator
     * @see Command#initialize()
     */
    protected void initialize() {
        super.initialize();
    }

    /**
     * Calculates desired motor output speed using PID controller.
     * @see Command#execute()
     */
    protected void execute() {
        super.execute();
        if(Elevator.getTicks() < setPoint) {
            Robot._elevator.elevate(-1);
        } else {
            Robot._elevator.elevate(1);
        }
    }
    /**
     * Checks if Command is done.  If it's done set elevator motor to 0
     * @return true means finished.  False means to call execute again
     * @see Command#isFinished()
     */
    @Override
    protected boolean isFinished() {
        if(Math.abs(setPoint - Elevator.getTicks()) < 1024) {
            Robot._elevator.elevate(0);
            return true;
        }
        return false;
    }
}
