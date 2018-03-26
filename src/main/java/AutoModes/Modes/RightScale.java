package AutoModes.Modes;

import AutoModes.Commands.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class RightScale extends CommandGroup {

    public RightScale() {
        System.err.println("RightScale.");
        addSequential(new MoveForward(176, Constants.FORWARD_LEFT_D));

        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/RightScale/_left_detailed.csv", "/home/lvuser/MotionProfiles/RightScale/_right_detailed.csv", 0.05));
        //addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/ActuallyRightScale/_left_detailed.csv", "/home/lvuser/MotionProfiles/ActuallyRightScale/_right_detailed.csv", 0.05));

        addSequential(new PointTurn(-45, false), 2);
        addSequential(new BangBang(35000, 0), 3.5);
        addSequential(new OutputTimed(0, 2));

        addSequential(new PointTurn(Constants.TWO_CUBE_ANGLE_RIGHT, true));
    }

}
