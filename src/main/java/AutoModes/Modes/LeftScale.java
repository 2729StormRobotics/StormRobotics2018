package AutoModes.Modes;

import AutoModes.Commands.ProfileFollower;
import Subsystems.NavX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Robot;

public class LeftScale extends CommandGroup {

    public LeftScale() {
        System.err.println("LeftScale.");
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/LeftScale/_left.csv"));
        //addSequential(new ProfileFollower(left, right, navx, Robot.traj));
    }

}