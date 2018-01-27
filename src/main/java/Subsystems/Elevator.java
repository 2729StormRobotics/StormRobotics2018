package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class Elevator extends Subsystem {

    public static final TalonSRX _left = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN); //formerly _leftMain
    public static final TalonSRX _right = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_2);  //formerly _left2


    public Elevator() {
        _right.follow(_left);
    }

    protected void initDefaultCommand() {

    }

    //@Override


    public static void elevate(double liftSpeed, boolean squareValues) {

        if (squareValues) {
            liftSpeed = Math.pow(liftSpeed, 2);
        }

        _left.set(ControlMode.PercentOutput, liftSpeed);


    }
}