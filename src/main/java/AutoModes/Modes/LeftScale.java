package AutoModes.Modes;

import AutoModes.Commands.*;
import Subsystems.NavX;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.Robot;

public class LeftScale extends CommandGroup {
    public LeftScale() {
        System.err.println("LeftScale.");
        Robot._driveTrain.gearShift(true);
        //addSequential(new MoveForward(176, Constants.FORWARD_LEFT_D));
        addSequential(new ScaleAndElevatorUp("LeftScaleAngled2", 0.065, 35000, 0.75));
        addSequential(new OutputTimed(0, 1));
        addSequential(new ArmState(false));
        addSequential(new BangBang(-35000, 0), 2);  //This needs to take elevator to the ground **Used to be 3 seconds **
        addSequential(new PointTurn(95, false)); //uncomment the other chunk for second cube auto
        //addSequential(new PointAndElevator(0.0, 95, false));
        //addSequential(new MoveForward(36, 0.0006)); //this one is to move out of the way for another robot doing cross scale


        /*
        addSequential(new MovingIntake(100));
        addSequential(new MoveForward(-100, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(-angleToCube));
        addSequential(new OutputTimed(0, 2));
        */

    }

}
