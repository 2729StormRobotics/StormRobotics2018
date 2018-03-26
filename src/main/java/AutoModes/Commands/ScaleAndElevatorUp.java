package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ScaleAndElevatorUp extends CommandGroup {
    public ScaleAndElevatorUp(String profileFolder, double kd, double bangHeight, double bangDelay) {
        addParallel(new ProfileFollower("/home/lvuser/MotionProfiles/"+ profileFolder +"/_left_detailed.csv",
            "/home/lvuser/MotionProfiles/"+ profileFolder +"/_right_detailed.csv", kd));
        addParallel(new BangBang(bangHeight, bangDelay), 3);
    }
}
