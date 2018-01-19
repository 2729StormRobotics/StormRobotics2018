package AutoModes.Modes;

import AutoModes.Commands.ProfileFollower;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftScale extends CommandGroup {

    public LeftScale(TalonSRX left, TalonSRX right, AHRS navx) {
        System.err.println("LeftScale.");
        addSequential(new ProfileFollower(left, right, navx, "/home/lvuser/MotionProfiles/LeftScale/_left.csv"));
    }

}
