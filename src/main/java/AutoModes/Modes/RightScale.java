package AutoModes.Modes;

import AutoModes.Commands.ProfileFollower;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightScale extends CommandGroup {

    public RightScale() {
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/RightScale/_left_detailed.csv",
            "/home/lvuser/MotionProfiles/RightScale/_right_detailed.csv"));
    }

}
