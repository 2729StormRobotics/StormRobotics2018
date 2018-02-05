package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevatorLeft = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN); //formerly _leftMain

    private static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    private static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);

    private boolean shooting = false;

    public Elevator() {
        _outputRight.follow(_outputLeft);
    }

    @Override
    protected void initDefaultCommand() {

    }


    public void elevate(double liftSpeed) {

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

    public  void outputToggle(){
        this.shooting = !this.shooting;

        if (shooting)
            _elevatorLeft.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
        LEDs.shooting = this.shooting;
    }
}
