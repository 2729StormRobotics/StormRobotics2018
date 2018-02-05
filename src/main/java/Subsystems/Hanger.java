package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class Hanger extends Subsystem {

    private static TalonSRX _hang;

    public Hanger() {
        _hang = new TalonSRX(Constants.PORT_MOTOR_DRIVE_HANG_MAIN);
    }

    protected void initDefaultCommand() {
    }

    public void setHanger(double speed) {
        if (Math.abs(speed) > 0.05)
            _hang.set(ControlMode.PercentOutput, speed);
        else
            _hang.set(ControlMode.PercentOutput, 0);
    }
}
