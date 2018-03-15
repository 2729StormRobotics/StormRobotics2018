package AutoModes.Modes;

import AutoModes.Commands.BangBang;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.ProfileFollower;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class LeftScaleAngled extends CommandGroup {

    public LeftScaleAngled() {
        System.err.println("LeftScaleAngled.");
        //addSequential(new MoveForward(176, Constants.FORWARD_LEFT_D));
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/LeftScaleAngled/_left_detailed.csv",
            "/home/lvuser/MotionProfiles/LeftScaleAngled/_right_detailed.csv", 0.05));
        addSequential(new BangBang(15000));

    }

}
