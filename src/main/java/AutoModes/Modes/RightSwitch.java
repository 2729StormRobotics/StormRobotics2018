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
        requires(Robot.navx);
        addSequential(new MoveForward(196.99));
        addSequential(new PointTurn(-90));
        addSequential(new MoveForward(20.6));
    }
}
