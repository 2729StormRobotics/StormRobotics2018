package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import util.CubeManipState;

import java.rmi.server.RemoteObject;

public class IntakeTimed extends Command {

    long startTime, addedTime;

    /**
     * A command that runs our intake for a set time.
     * @param seconds time in seconds to run intake
     */
    public IntakeTimed(long seconds) {
        startTime = System.currentTimeMillis();
        addedTime = seconds * 1000;
    }

    /**
     * The function that runs every time Command is called.  Sets output to true and records starting time
     * @see Command#initialize()
     */
    @Override
    protected void initialize() {
        System.out.println("Should be output");
        startTime = System.currentTimeMillis();
        super.initialize();
        Robot._elevator.setOutput(CubeManipState.OUT);
    }

    /**
     * Execute runs periodically.  This just ensures the cart is outputting.
     * @see Command#execute()
     */
    @Override
    protected void execute() {
        System.out.println("IntakeTimed: execute");
        super.execute();
        Robot._elevator.setOutput(CubeManipState.OUT);
    }

    /**
     * End is called after elapsed time.  Turns cart motors off.
     * @see Command#end()
     */
    @Override
    protected void end() {
        super.end();
        Robot._elevator.setOutput(CubeManipState.IDLE);
        System.out.println("IntakeTimed: end");
    }

    /**
     * If the Command is interrupted we call end()
     * @see Command#interrupted()
     */
    @Override
    protected void interrupted() {
        super.interrupted();
        end();
        System.out.println("IntakeTimed: interrupted");
    }

    /**
     * Checks after each execute.  If elapsed time has passed call end()
     * @return True means finished.  False means to run execute again
     * @see Command#isFinished()
     */
    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis() >= startTime + addedTime) {
            System.out.println("IntakeTimed: isFinished");
            end();
            return true;
        }
        return false;
    }
}
