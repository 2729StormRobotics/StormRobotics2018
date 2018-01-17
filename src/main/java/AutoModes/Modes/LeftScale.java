package AutoModes.Modes;

import AutoModes.Commands.ProfileFollower;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.CommandGroup;

import java.io.File;

public class LeftScale extends CommandGroup {

    TalonSRX left, right;

    public LeftScale(TalonSRX left, TalonSRX right, AHRS navx){

        addSequential(new ProfileFollower(left, right, navx, "_left.csv"));

    }

}
