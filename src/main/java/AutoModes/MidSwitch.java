package AutoModes;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidSwitch extends CommandGroup{
        public MidSwitch(TalonSRX left, TalonSRX right){
            TalonSRX leftMotor = left;
            TalonSRX rightMotor = right;
        }
}
