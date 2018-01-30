package AutoModes.Modes;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.ProfileFollower;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftScale extends CommandGroup {

    public LeftScale() {
        System.err.println("LeftScale.");
        addSequential(new MoveForward(176));
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/LeftScale/_left_detailed.csv",
                "/home/lvuser/MotionProfiles/LeftScale/_right_detailed.csv"));

    }

}