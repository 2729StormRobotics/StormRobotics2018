package AutoModes.Modes;

import AutoModes.Commands.OutputTimed;
import AutoModes.Commands.ProfileFollower;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidSwitch extends CommandGroup {

    public MidSwitch(char side) {
        System.err.println("MidSwitch.");

        if(side == 'R') {
            addParallel(new ProfileFollower("/home/lvuser/MotionProfiles/MidRightSwitch/_left_detailed.csv",
                    "/home/lvuser/MotionProfiles/MidRightSwitch/_right_detailed.csv", 0.05));
            addParallel(new OutputTimed( 2.5, 1));
        }
        else if(side == 'L'){
            addParallel(new ProfileFollower("/home/lvuser/MotionProfiles/MidLeftSwitch/_left_detailed.csv",
                    "/home/lvuser/MotionProfiles/MidLeftSwitch/_right_detailed.csv", 0.05));
            addParallel(new OutputTimed(2.5,1));
        }
    }
}
