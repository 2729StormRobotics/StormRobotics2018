package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import robot.Constants;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevatorLeft = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN); //formerly _leftMain

    private static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    private static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);

    private boolean shooting = false;
    private static final AnalogPotentiometer pot = new AnalogPotentiometer(0);
    private double startHeight = 0;

    public Elevator() {
        startHeight = getPotHeight();
        _outputRight.follow(_outputLeft);
    }

    @Override
    protected void initDefaultCommand() {
    }


    public void elevate(double liftSpeed) {

        _elevatorLeft.set(ControlMode.PercentOutput, liftSpeed);

        if(liftSpeed > 0){
            LEDs.elevatingUp = true;
        } else if(liftSpeed < 0){
            LEDs.elevatingUp = false;
        }

    }

    public void outputToggle(){
        this.shooting = !this.shooting;

        if (shooting)
            _elevatorLeft.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
        LEDs.shooting = this.shooting;
    }

    public void outputSet(boolean _shooting) {
        this.shooting = _shooting;

        if (shooting)
            _elevatorLeft.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
    }

    public static double getPotHeight() {
        return pot.get();
    }

    public double get() {
        return getPotHeight() + _elevatorLeft.getSelectedSensorPosition(0);
    }
}
