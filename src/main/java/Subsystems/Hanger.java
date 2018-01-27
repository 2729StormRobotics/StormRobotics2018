package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class Hanger extends Subsystem {


    public static final TalonSRX _hang = new TalonSRX(Constants.PORT_MOTOR_DRIVE_HANG_MAIN); //formerly _leftMain
    //public static final TalonSRX _right = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_2);  //formerly _left2


    public Hanger() {
    }

    protected void initDefaultCommand() {

    }

    public static void pull(double speed) {
        if (Math.abs(speed) > 0.05)
            _hang.set(ControlMode.PercentOutput, speed);
        else
            _hang.set(ControlMode.PercentOutput, 0);
    }

    //@Override
    public static void hang(double pullSpeed, boolean squareValues, double tolerance) {

        if (squareValues) {
            pullSpeed = Math.pow(pullSpeed, 2);
        }

        _hang.set(ControlMode.PercentOutput, pullSpeed);

    }


}
