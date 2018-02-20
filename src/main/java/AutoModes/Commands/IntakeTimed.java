package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import util.CubeManipState;
public class IntakeTimed extends Command {

    long startTime, addedTime, delay, endTime;

    /**
     * A command that runs our intake for a set time.
     * @param seconds time in seconds to run intake
     */
    public IntakeTimed(long _delay, long seconds) {
        delay = _delay * 1000;
        addedTime = seconds * 1000;
    }

    /**
     * The function that runs every time Command is called.  Sets output to true and records starting time
     * @see Command#initialize()
     */
    @Override
    protected void initialize() {
        System.out.println("Should be output");
        startTime = System.currentTimeMillis() + delay;
        super.initialize();
        endTime = startTime + addedTime;
    }

    /**
     * Execute runs periodically.  This just ensures the cart is outputting.
     * @see Command#execute()
     */
    @Override
    protected void execute() {
        System.out.println("IntakeTimed: execute");
        super.execute();

        if(System.currentTimeMillis() >= startTime)
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
        if(System.currentTimeMillis() >= endTime) {
            System.out.println("IntakeTimed: isFinished");
            end();
            return true;
        }
        return false;
    }
}
