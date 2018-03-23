package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import util.CubeManipState;

import static robot.Robot._intake;

public class IntakeCube extends Command {
    public IntakeCube() {
    }

    /**
     * The function that runs every time Command is called.  Sets output to true and records starting time
     * @see Command#initialize()
     */
    @Override
    protected void initialize() {
        System.out.println("Should be output");
        super.initialize();
    }

    /**
     * Execute runs periodically.  This just ensures the arms are moving inward
     * @see Command#execute()
     */
    @Override
    protected void execute() {
        System.out.println("OutputTimed: execute");
        super.execute();

        _intake.setIntake(CubeManipState.IN);
    }

    /**
     * End is called after elapsed time.  Turns intake and cart motors off
     * @see Command#end()
     */
    @Override
    protected void end() {
        super.end();
        _intake.setIntake(CubeManipState.IDLE);
        System.out.println("OutputTimed: end");
    }

    /**
     * If the Command is interrupted we call end()
     * @see Command#interrupted()
     */
    @Override
    protected void interrupted() {
        super.interrupted();
        end();
        System.out.println("OutputTimed: interrupted");
    }

    /**
     * Checks after each execute.  If cube has been detected call end()
     * @return True means finished.  False means to run execute again
     * @see Command#isFinished()
     */
    @Override
    protected boolean isFinished() {
        /*
        if(Robot._proxSens.getValue() >= 690) {
            Robot._intake.setIntake(CubeManipState.IDLE);
            System.out.println("OutputTimed: isFinished");
            end();
            return true;
        }
        */
        return false;
    }
}
