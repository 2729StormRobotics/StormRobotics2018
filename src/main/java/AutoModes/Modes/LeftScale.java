package AutoModes.Modes;

import AutoModes.Commands.ProfileFollower;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftScale extends CommandGroup {

    public LeftScale() {
        System.err.println("LeftScale.");
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/TurnAround/_left_detailed.csv",
                "/home/lvuser/MotionProfiles/TurnAround/_right_detailed.csv"));
        //addSequential(new ProfileFollower(left, right, navx, Robot.traj));
    }

}