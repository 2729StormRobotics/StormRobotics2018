package AutoModes.Modes;

import AutoModes.Commands.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class LeftScale extends CommandGroup {

    public LeftScale() {
        System.err.println("LeftScale.");
        addSequential(new MoveForward(176, Constants.FORWARD_LEFT_D));
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/LeftScale/_left_detailed.csv",
                "/home/lvuser/MotionProfiles/LeftScale/_right_detailed.csv", 0.05));
        addSequential(new PointTurn(45), 2);
        addSequential(new BangBang(10000), 3.5);
        addSequential(new IntakeTimed(0, 2));

    }

}
