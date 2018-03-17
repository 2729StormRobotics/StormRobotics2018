package AutoModes.Commands;

import Subsystems.Elevator;
import edu.wpi.first.wpilibj.command.Command;
import robot.Constants;
import robot.Robot;
import util.CubeManipState;

public class BangBang extends Command {

    double setPoint, height, delay, startTime, endTime, addedTime;


    public BangBang(double _height, double _delay) {
        delay = _delay * 1000;
        height = _height;
    }

    public synchronized void start() {
        super.start();
        startTime = System.currentTimeMillis() + delay;
        endTime = startTime;

    }
    protected void end() {
        super.end();
        Robot._elevator.elevate(0);
    }

    /**
     * In the case the Command is interrupted turn off Elevator
     * @see Command#interrupted()
     */
    @Override
    protected void interrupted() {
        super.interrupted();
    }


    protected void initialize() {
        super.initialize();
        setPoint = Elevator.getTicks() + height;
        System.out.println("setpoint: " + setPoint);
        System.out.println("starting: " + Elevator.getTicks());

        System.err.println("start BangBang");
    }

    /**
     * Calculates desired motor output speed using PID controller.
     * @see Command#execute()
     */
    protected void execute() {
        System.out.println("Simmer is " + Elevator.getTicks());
        super.execute();
        //if(System.currentTimeMillis() >= startTime) {
            if(Elevator.getTicks() < setPoint) {
                Robot._elevator.elevate(-0.7);
            } else {
                Robot._elevator.elevate(0.7);
            }
        //}
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
            System.out.println("ending: " + Elevator.getTicks());
            return true;
        }
        return false;
    }
}
