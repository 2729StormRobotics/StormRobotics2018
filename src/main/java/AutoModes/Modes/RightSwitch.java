package AutoModes.Modes;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import Subsystems.DriveTrain;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Robot;

public class RightSwitch extends CommandGroup {

    public RightSwitch() {
        addSequential(new MoveForward(154));
        addSequential(new PointTurn(-90));
        addSequential(new MoveForward(20.6));
    }
}
