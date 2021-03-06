package AutoModes.Modes;

import AutoModes.Commands.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.Robot;

public class RightScale extends CommandGroup {

    public RightScale() {
        System.err.println("RightScale.");
        Robot._driveTrain.gearShift(true);
        addSequential(new ScaleAndElevatorUp("RightScaleAngled", 0.065, 35000, 0.75));
        //addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/ActuallyRightScale/_left_detailed.csv", "/home/lvuser/MotionProfiles/ActuallyRightScale/_right_detailed.csv", 0.05));

        //addSequential(new PointTurn(-45, false), 2);
        addSequential(new OutputTimed(0, 1));

        addSequential(new ArmState(false));
        addSequential(new BangBang(-35000, 0), 2.0);
        addSequential(new MoveForward(-50, 0.0006));

        //addSequential(new PointTurn(Constants.TWO_CUBE_ANGLE_RIGHT, true));
    }

}
