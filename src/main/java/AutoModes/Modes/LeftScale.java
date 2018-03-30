package AutoModes.Modes;

import AutoModes.Commands.*;
import Subsystems.NavX;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.Robot;

public class LeftScale extends CommandGroup {
    public LeftScale() {
        System.err.println("LeftScale.");
        //addSequential(new MoveForward(176, Constants.FORWARD_LEFT_D));
        addSequential(new ScaleAndElevatorUp("LeftScaleAngled2", 0.05, 35000, 0));
        addSequential(new OutputTimed(0, 2));
        addSequential(new ArmState(false));
        addSequential(new BangBang(-35000, 0), 3);  //This needs to take elevator to the ground
        addSequential(new PointTurn(100, false)); //uncomment the other chunk for second cube auto

        //addSequential(new MoveForward(-40, 0.05)); this one is to move out of the way for another robot doing cross scale

        /*
        addSequential(new MovingIntake(100));
        addSequential(new MoveForward(-100, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(-angleToCube));
        addSequential(new OutputTimed(0, 2));
        */

    }

}
