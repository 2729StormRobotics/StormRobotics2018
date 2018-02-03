package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevatorLeft = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN); //formerly _leftMain
    public static final TalonSRX _elevatorRight = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_2);  //formerly _left2

    public static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    public static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);

    public Elevator() {
        _elevatorRight.follow(_elevatorLeft);
        _outputRight.follow(_outputLeft);
    }

    @Override
    protected void initDefaultCommand() {

    }


    public static void elevate(double liftSpeed, boolean squareValues) {

        if (squareValues) {
            liftSpeed = Math.pow(liftSpeed, 2);
        }

        _elevatorLeft.set(ControlMode.PercentOutput, liftSpeed);

        if(liftSpeed > 0){
            LEDs.elevating = true;
            LEDs.elevatingUp = true;
        } else if(liftSpeed < 0){
            LEDs.elevating = true;
            LEDs.elevatingUp = false;
        } else {
            LEDs.elevating = false;
        }

    }

    public static void output(double dPadValue){
        double outputSpeed = 0.0;
        if(dPadValue == 315 || dPadValue == 0 || dPadValue == 45){
            outputSpeed = 1.0;
        }
        else{
            outputSpeed = 0.0;
        }
        _elevatorLeft.set(ControlMode.PercentOutput, outputSpeed);

        if(outputSpeed > 0){
            LEDs.shooting = true;
        } else {
            LEDs.shooting = false;
        }
    }
}
