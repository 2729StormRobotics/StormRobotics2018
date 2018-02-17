package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import util.CubeManipState;

import java.rmi.server.RemoteObject;

public class IntakeTimed extends Command {

    long startTime, addedTime;

    public IntakeTimed(long seconds) {
        startTime = System.currentTimeMillis();
        addedTime = seconds * 1000;
    }

    @Override
    protected void initialize() {
        System.out.println("Should be output");
        startTime = System.currentTimeMillis();
        super.initialize();
        Robot._elevator.setOutput(CubeManipState.OUT);
    }

    @Override
    protected void execute() {
        System.out.println("IntakeTimed: execute");
        super.execute();
        Robot._elevator.setOutput(CubeManipState.OUT);
    }

    @Override
    protected void end() {
        super.end();
        Robot._elevator.setOutput(CubeManipState.IDLE);
        System.out.println("IntakeTimed: end");
    }

    @Override
    protected void interrupted() {
        super.interrupted();
        end();
        System.out.println("IntakeTimed: interrupted");
    }

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
