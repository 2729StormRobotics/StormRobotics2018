package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

import javax.naming.ldap.Control;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevatorLeft = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN);

    private static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    private static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);

    private boolean shooting = false;
    private boolean checkTicks;
    private static final AnalogPotentiometer pot = new AnalogPotentiometer(0);

    private double startPos = 0, switchPos = 0,  zeroPos = 0;
    private double height;
    private double startTicks;
    private double ticksStageOneMax;

    public Elevator() {
        double startPos = getPotHeightInches();
        double zeroPos = _elevatorLeft.getSelectedSensorPosition(0) - inchToTicks(startPos);
        _outputRight.follow(_outputLeft);
        height = 0.0;
        startTicks = _elevatorLeft.getSelectedSensorPosition(0);
        checkTicks = true;
    }

    @Override
    protected void initDefaultCommand() {
    }


    public void elevate(double liftSpeed) {
        height = get();
        _elevatorLeft.set(ControlMode.PercentOutput, liftSpeed);

        if(liftSpeed > 0){
            LEDs.elevatingUp = true;
        } else if(liftSpeed < 0){
            LEDs.elevatingUp = false;
        }

    }

    public void output(double speed){
//        this.shooting = !this.shooting;
//
//        if (shooting)
//            _elevatorLeft.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
//        LEDs.shooting = this.shooting;
        _elevatorLeft.set(ControlMode.PercentOutput, speed);

    }

    public void outputSet(boolean _shooting) {
        this.shooting = _shooting;

        if (shooting)
            _elevatorLeft.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
    }

    public static double getPotHeight() {
        return pot.get();
    }

    public double getInches() {
        if (getPotHeight() >= Constants.STRPOT_SWITCH_FRACTION) {
            return _elevatorLeft.getSelectedSensorPosition(0) / Constants.TICKS_PER_REV;
        } else {

        }
        return 0;
    }

    private static double getPotHeightInches() {
        return pot.get() * Constants.STRPOT_MAX;
    }

    private double inchToTicks(double pos) {
        return pos / Constants.STRPOT_TICKS_PER_INCH;
    }

    private void checkSwitch() {
        if (pot.get() == Constants.STRPOT_SWITCH_FRACTION) {
            switchPos = getPotHeightInches();
        }
    }

    private double get() {
        if(getPotHeightInches() < Constants.STAGE_ONE_MAX){
            checkTicks = true;
            return getPotHeightInches();
        } else {
            if(checkTicks){
                ticksStageOneMax = _elevatorLeft.getSelectedSensorPosition(0);
                checkTicks = false;
            }
            double temp = (_elevatorLeft.getSelectedSensorPosition(0) - ticksStageOneMax) / Constants.STRPOT_TICKS_PER_INCH;
            return getPotHeightInches() + temp;
        }
    }
}

