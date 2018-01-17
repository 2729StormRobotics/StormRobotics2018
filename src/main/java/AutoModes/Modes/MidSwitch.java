package AutoModes.Modes;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidSwitch extends CommandGroup{
    TalonSRX leftMotor;
    TalonSRX rightMotor;

        public MidSwitch(TalonSRX left, TalonSRX right, AHRS ahrs){
            TalonSRX leftMotor = left;
            TalonSRX rightMotor = right;
        }
}
