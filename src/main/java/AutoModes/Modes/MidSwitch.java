package AutoModes.Modes;

import AutoModes.Commands.ProfileFollower;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidSwitch extends CommandGroup {

    public MidSwitch(char side) {
        System.err.println("MidSwitch.");

        if(side == 'R') {
            addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/MidRightSwitch/_left_detailed.csv",
                    "/home/lvuser/MotionProfiles/MidRightSwitch/_right_detailed.csv"));
        }
        else if(side == 'L'){
            addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/MidLeftSwitch/_left_detailed.csv",
                    "/home/lvuser/MotionProfiles/MidLeftSwitch/_right_detailed.csv"));
        }
    }
}
