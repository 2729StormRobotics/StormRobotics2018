package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidLeftShoot extends CommandGroup {

    public MidLeftShoot() {
        addParallel(new ProfileFollower("/home/lvuser/MotionProfiles/MidRightSwitch/_left_detailed.csv",
            "/home/lvuser/MotionProfiles/MidRightSwitch/_right_detailed.csv", 0.0636));
        addParallel(new OutputTimed( 3.0, 1));
    }

}
