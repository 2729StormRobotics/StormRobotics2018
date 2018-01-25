package AutoModes.Modes;

import AutoModes.Commands.ProfileFollowerOld;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestMode extends CommandGroup{

    public TestMode(){
        System.err.println("MidSwitch.");
        addSequential(new ProfileFollowerOld("/home/lvuser/MotionProfiles/RightScaleBack/_left_detailed.csv",
                "/home/lvuser/MotionProfiles/RightScaleBack/_right_detailed.csv"));
    }

}
