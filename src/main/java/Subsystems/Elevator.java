package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Robot;
import util.CubeManipState;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevator = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN);
    public static final TalonSRX _elevatorFollow = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_2);

    private static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    private static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);
    private boolean checkTicks;
    private static final AnalogPotentiometer pot = new AnalogPotentiometer(Constants.PORT_STRING_POT);
    public CubeManipState state;

    public static double switchPos, zeroPos, maxPos;

    public Elevator() {
        _elevatorFollow.follow(_elevator);
        zeroPos = _elevator.getSelectedSensorPosition(0) - (((pot.get() - Constants.STRPOT_START_FRACTION) * Constants.STRPOT_MAX) / Constants.ELEVATOR_TICKS_PER_INCH);
        maxPos = zeroPos + (Constants.ELEVATOR_MAX_TICKS);
        _outputLeft.follow(_outputRight);
        checkTicks = true;
    }

    @Override
    protected void initDefaultCommand() {
    }


    public void elevate(double liftSpeed) {
//        if(_elevator.getSelectedSensorPosition(0) >= zeroPos || _elevator.getSelectedSensorPosition(0) <= maxPos) {
//
//        System.out.println(_elevator.getSelectedSensorPosition(0));

        if(pot.get() < Constants.STRPOT_START_FRACTION && liftSpeed > 0) {
            liftSpeed = 0;
            updateBounds();
            System.out.println("ZeroPos: " + zeroPos);
        }

        if(pot.get() < Constants.ELEVATOR_SLOW_DOWN_FRACTION && liftSpeed > 0) {
            liftSpeed = 0.25;
        }

        _elevator.set(ControlMode.PercentOutput, liftSpeed);

        if(liftSpeed > 0) {
            LEDs.elevatingUp = true;
        } else if(liftSpeed < 0){
            LEDs.elevatingUp = false;
        }
    }

    public void toggleOutput(){
        if (state != CubeManipState.IN)
            setOutput(CubeManipState.IN);
        else
            setOutput(CubeManipState.IDLE);

        LEDs.shooting = (state == CubeManipState.OUT);

    }

    public void setOutput(CubeManipState desiredState) {
        if(desiredState == CubeManipState.IN) {
            _outputRight.set(ControlMode.PercentOutput, -Constants.CART_IN_SPEED);
            state = CubeManipState.IN;
        } else if (desiredState == CubeManipState.OUT) {
            _outputRight.set(ControlMode.PercentOutput, Constants.OUTPUT_SPEED);
            state = CubeManipState.OUT;
            //if(getHeight() > zeroPos)
                //Robot._intake.setIntake(CubeManipState.OUT);
        } else {
            _outputRight.set(ControlMode.PercentOutput, 0);
            state = CubeManipState.IDLE;
        }
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

    public static double getPotFrac() {
        //System.out.println(pot.get());
        return pot.get();
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

    private static void updateBounds() {
        zeroPos = _elevator.getSelectedSensorPosition(0);
        maxPos = zeroPos + (Constants.ELEVATOR_MAX_TICKS);
    }

    private static double getHypotInches() {
        return pot.get() * Constants.STRPOT_MAX;
    }

    private static double convertHypotToY() {
        return Math.sqrt(Math.pow(getHypotInches(), 2) - 1);
    }
}

/* Different approach: Get the fraction of the string pot at starting height and get the max fraction that it extends. If stringPot.get() > than the max value, use encoders*/
