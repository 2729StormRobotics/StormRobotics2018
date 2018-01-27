package AutoModes.Modes;

import AutoModes.Commands.ProfileFollower;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestMode extends CommandGroup {

    public TestMode() {
        System.err.println("MidSwitch.");
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/TenFoot/_left_detailed.csv", "/home/lvuser/MotionProfiles/TenFoot/_right_detailed.csv"));
    }

}
