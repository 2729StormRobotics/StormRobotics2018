package AutoModes.Commands;

import Subsystems.Elevator;
import edu.wpi.first.wpilibj.command.Command;
import robot.Constants;
import robot.Robot;

public class BangBang extends Command {

    double setPoint, height;


    public BangBang(double _height) {
        height = _height;
    }

    public synchronized void start() {
        setPoint = Elevator.getTicks() + height;
        System.out.println("setpoint: " + setPoint);
        System.out.println("starting: " + Elevator.getTicks());
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
        //System.out.println(Elevator.getTicks());
        super.execute();
        if(Elevator.getTicks() < setPoint) {
            Robot._elevator.elevate(-0.7);
        } else {
            Robot._elevator.elevate(0.7);
        }
    }
    /**
     * Checks if Command is done.  If it's done set elevator motor to 0
     * @return true means finished.  False means to call execute again
     * @see Command#isFinished()
     */
    @Override
    protected boolean isFinished() {
        if(Math.abs(setPoint - Elevator.getTicks()) < (1024 * 2)) {
            Robot._elevator.elevate(0);
            return true;
        }
        return false;
    }
}
