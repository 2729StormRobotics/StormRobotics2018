package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

import javax.naming.ldap.Control;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevator = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN);
    public static final TalonSRX _elevatorFollow = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_2);

    private static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    private static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);

    private boolean shooting = false;
    private boolean checkTicks;
    private static final AnalogPotentiometer pot = new AnalogPotentiometer(0);

    private static double switchPos, startPos;

    public Elevator() {
        _elevatorFollow.follow(_elevator);
        startPos = getHypotInches();
        _outputRight.follow(_outputLeft);
        checkTicks = true;
    }

    @Override
    protected void initDefaultCommand() {
    }


    public void elevate(double liftSpeed) {
        _elevator.set(ControlMode.PercentOutput, liftSpeed);
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
        _elevator.set(ControlMode.PercentOutput, speed);

    }

    public void outputSet(boolean _shooting) {
        this.shooting = _shooting;

        if (shooting)
            _elevator.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
    }

    private static double getPotInches() { return convertHypotToY(); }

//    private double get() { Old Juan
//        if(getPotInches() <= Constants.STAGE_ONE_MAX){
//            checkTicks = true;
//            return getPotInches();
//        } else {
//            if(checkTicks){
//                ticksStageOneMax = _elevator.getSelectedSensorPosition(0);
//                checkTicks = false;
//            }
//            double temp = (_elevator.getSelectedSensorPosition(0) - ticksStageOneMax) / Constants.STRPOT_TICKS_PER_INCH;
//            return getHypotInches() + temp;
//        }
//    }

    public static double getHeight() {
        if(pot.get() <= Constants.STRPOT_SWITCH_FRACTION) {
            updateSwitchPos();
            return getPotInches();
        } else {
            return ((_elevator.getSelectedSensorPosition(0) - switchPos) / Constants.STRPOT_TICKS_PER_INCH) + getPotInches();
        }
    }

    private static void updateSwitchPos() {
        if (pot.get() >= Constants.STRPOT_SWITCH_FRACTION) {
            switchPos = _elevator.getSelectedSensorPosition(0);
        }
    }

    private static double getHypotInches() {
        return pot.get() * Constants.STRPOT_MAX;
    }

    private static double convertHypotToY() {
        return Math.sqrt(Math.pow(getHypotInches(), 2) - 1);
    }
}

/* Different approach: Get the fraction of the string pot at starting height and get the max fraction that it extends. If stringPot.get() > than the max value, use encoders*/
