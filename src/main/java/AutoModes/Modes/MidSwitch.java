package AutoModes.Modes;

import AutoModes.Commands.ProfileFollowerOld;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidSwitch extends CommandGroup{

        public MidSwitch(){
                System.err.println("MidSwitch.");
                addSequential(new ProfileFollowerOld("/home/lvuser/MotionProfiles/MidRightSwitch/_left_detailed.csv",
                        "/home/lvuser/MotionProfiles/MidRightSwitch/_right_detailed.csv"));

        }
}
