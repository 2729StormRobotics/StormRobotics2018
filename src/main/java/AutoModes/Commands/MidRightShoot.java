package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidRightShoot extends CommandGroup {

    public MidRightShoot() {
        addParallel(new ProfileFollower("/home/lvuser/MotionProfiles/MidLeftSwitch/_left_detailed.csv",
            "/home/lvuser/MotionProfiles/MidLeftSwitch/_right_detailed.csv", 0.05));
        addParallel(new OutputTimed(3.25,1));
    }

}
