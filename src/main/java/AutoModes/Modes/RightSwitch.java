package AutoModes.Modes;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import Subsystems.DriveTrain;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightSwitch extends CommandGroup {

    public RightSwitch() {
        addSequential(new MoveForward(196.99, DriveTrain._leftMain, DriveTrain._rightMain));
        addSequential(new PointTurn(-90, DriveTrain._leftMain,DriveTrain._rightMain));
        addSequential(new MoveForward(20.6, DriveTrain._leftMain, DriveTrain._rightMain));
    }
}
