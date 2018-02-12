package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevator = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN);
    public static final TalonSRX _elevatorFollow = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_2);

    private static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    private static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);

    private boolean shooting = false;
    private boolean checkTicks;
    private static final AnalogPotentiometer pot = new AnalogPotentiometer(Constants.PORT_STRING_POT);

    private static double switchPos, zeroPos, maxPos;

    public Elevator() {
        _elevatorFollow.follow(_elevator);
        zeroPos = _elevator.getSelectedSensorPosition(0) - (((pot.get() - Constants.STRPOT_START_FRACTION) * Constants.STRPOT_MAX) / Constants.ELEVATOR_TICKS_PER_INCH);
        maxPos = zeroPos + (Constants.ELEVATOR_MAX_TICKS);
        _outputRight.follow(_outputLeft);
        checkTicks = true;
    }

    @Override
    protected void initDefaultCommand() {
    }


    public void elevate(double liftSpeed) {
        if(_elevator.getSelectedSensorPosition(0) >= zeroPos || _elevator.getSelectedSensorPosition(0) <= maxPos)
            _elevator.set(ControlMode.PercentOutput, liftSpeed);
        if(liftSpeed > 0) {
            LEDs.elevatingUp = true;
        } else if(liftSpeed < 0){
            LEDs.elevatingUp = false;
        }
    }

    public void output(){
        this.shooting = !this.shooting;
        System.out.println("Shooting: " + shooting);

        if (shooting)
            _outputLeft.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
        else
            _outputLeft.set(ControlMode.PercentOutput, 0);
        LEDs.shooting = this.shooting;

    }

    public void outputSet(boolean _shooting) {
        this.shooting = _shooting;

        if (shooting)
            _elevator.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
    }

    private static double getPotInches() { return convertHypotToY(); }

    public static double getHeight() {
        if(pot.get() <= Constants.STRPOT_SWITCH_FRACTION) {
            updateSwitchPos();
            return getPotInches();
        } else {
            return ((_elevator.getSelectedSensorPosition(0) - switchPos) / Constants.ELEVATOR_TICKS_PER_INCH) + getPotInches();
        }
    }

    public static double checkHeight(double ticks) {
        if(_elevator.getSelectedSensorPosition(0) + ticks <= zeroPos) {
            return zeroPos;
        } else if(_elevator.getSelectedSensorPosition(0) + ticks >= maxPos) {
            return maxPos;
        }
        return _elevator.getSelectedSensorPosition(0) + ticks;
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
