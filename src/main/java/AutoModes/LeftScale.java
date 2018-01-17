package AutoModes;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.CommandGroup;

import java.io.File;

public class LeftScale extends CommandGroup {

    TalonSRX left, right;

    public LeftScale(){

        addSequential(new ProfileFollower("_left.csv"));

    }

}
