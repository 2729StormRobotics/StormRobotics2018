package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import util.CubeManipState;

import javax.naming.ldap.Control;

import static robot.Robot._controller;
import static robot.Robot._intake;

public class Elevator extends Subsystem {

    public static final TalonSRX _elevator = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_MAIN);
    public static final TalonSRX _elevatorFollow = new TalonSRX(Constants.PORT_MOTOR_DRIVE_ELEVATOR_2);

    private static final TalonSRX _outputLeft = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_LEFT);
    private static final TalonSRX _outputRight = new TalonSRX(Constants.PORT_MOTOR_OUTPUT_RIGHT);
    private static final AnalogPotentiometer pot = new AnalogPotentiometer(Constants.PORT_STRING_POT);

    public boolean armsUp;

    public CubeManipState state;

    public static double switchPos, zeroPos, maxPos, startPos;

    /**
     * The Elevator subsystem. Controls vertical movement of elevator and the cart that ejects the cube.
     */
    public Elevator() {
        _elevatorFollow.follow(_elevator);
        //zeroPos = _elevator.getSelectedSensorPosition(0) - (((pot.get() - Constants.STRPOT_START_FRACTION) * Constants.STRPOT_MAX) / Constants.ELEVATOR_TICKS_PER_INCH);
        maxPos = zeroPos + (Constants.ELEVATOR_ENCODER_RANGE);
        _outputLeft.follow(_outputRight);
        _elevator.configPeakCurrentLimit(35, 500);
        _elevatorFollow.configPeakCurrentLimit(35, 500);
        _outputLeft.configPeakCurrentLimit(25, 500);
        _outputRight.configPeakCurrentLimit(25, 500);
        _elevator.setSensorPhase(true);
        armsUp = false;
    }

    public void setStartPos(){
        startPos = _elevator.getSelectedSensorPosition(0);
        System.out.println(startPos);
    }

    @Override
    protected void initDefaultCommand() {
    }

    /**
     * Moves elevator at set speed.
     * @param liftSpeed the speed to lift the elevator. Positive moves down. Negative moves up.
     */
    public void elevate(double liftSpeed) {

        //if((pot.get() < Constants.ELEVATOR_SLOW_DOWN_FRACTION && liftSpeed > 0) /*|| (getPercentageHeight() > 0.9 && liftSpeed < 0)*/) { liftSpeed = 0.10; }

        //if((pot.get() < (Constants.ELEVATOR_SLOW_DOWN_FRACTION * 2) && liftSpeed > 0) /*|| (getPercentageHeight() > 0.9 && liftSpeed < 0)*/) { liftSpeed = 0.30; }
        //if(_elevator.getSelectedSensorPosition(0) >= maxPos) { liftSpeed = 0.0; }
        /*if(pot.get() < Constants.STRPOT_START_FRACTION && liftSpeed > 0) {
            liftSpeed = 0;
            updateBounds();
            System.out.println("ZeroPos: " + zeroPos);
        }*/

        //System.out.println(Elevator.getPotFrac());


        _elevator.set(ControlMode.PercentOutput, liftSpeed);

        if(liftSpeed > 0) {
            LEDs.elevatingUp = true;
        } else if(liftSpeed < 0){
            LEDs.elevatingUp = false;
        }

        if(armsUp == false && _elevator.getSelectedSensorPosition(0) > startPos + 500) {
            armsUp = false;
//            _intake.toggleIntakeArm();
            System.out.println("Arms should be going up");
        } else if(armsUp == true && _elevator.getSelectedSensorPosition(0) <= startPos + 300) {
            armsUp = true;
//            _intake.toggleIntakeArm();
            System.out.println("Arms should be going down");
        }

    }

    /**
     * Changed intake between IN and IDLE state.
     */
    public void toggleOutput(double speed){
        if (state != CubeManipState.IN)
            setOutput(CubeManipState.IN, speed);
        else
            setOutput(CubeManipState.IDLE, speed);

        LEDs.shooting = (state == CubeManipState.OUT);

    }

    public void setArmsUp(boolean arm) {
        armsUp = arm;
    }

    /**
     * Sets state of Cart, either IN, REVERSE or OFF
     * @param desiredState CubeManipState.IN moves inward, CubeManipState.OUT moves outward, CubeManipState.IDLE is off
     */
    public void setOutput(CubeManipState desiredState, double speed) {
        if(desiredState == CubeManipState.IN) {
            _outputRight.set(ControlMode.PercentOutput, -Constants.CART_IN_SPEED);
            state = CubeManipState.IN;
        } else if (desiredState == CubeManipState.OUT) {
            _outputRight.set(ControlMode.PercentOutput, speed);
            state = CubeManipState.OUT;
        } /*else if (desiredState == CubeManipState.CLOCKWISE) {
            _outputRight.set(ControlMode.PercentOutput, speed);
            _outputLeft.set(ControlMode.PercentOutput, -speed);
            state = CubeManipState.CLOCKWISE;
        } else if (desiredState == CubeManipState.COUNTERCLOCKWISE) {
            _outputRight.set(ControlMode.PercentOutput, -speed);
            _outputLeft.set(ControlMode.PercentOutput, speed);
            state = CubeManipState.COUNTERCLOCKWISE;
        } */else {
            _outputRight.set(ControlMode.PercentOutput, 0);
            state = CubeManipState.IDLE;
        }
    }

    /**
     * Returns the percentage that the elevator is elevated relative to the total rotations that the elevator elevator can rotate.
     * @return percentage the elevator is elevated between 0 and 1.
     */
    public static double getPercentageHeight() {
        return (_elevator.getSelectedSensorPosition(0) - zeroPos) / Constants.ELEVATOR_ENCODER_RANGE;
    }

    /**
     * Gets fraction of String Pot extended.
     * @return the fraction of String Pot extended.
     */
    public static double getPotFrac() {
        return pot.get();
    }

    /**
     * Gets the current encoder value.
     * @return current encoder value.
     */
    public static double getTicks() {
        return _elevator.getSelectedSensorPosition(0);
    }

    /**
     * Returns height in ticks relative to zeroPos
     * @return height in ticks relative to zeroPos
     */
    public double getHeight() {
        return _elevator.getSelectedSensorPosition(0) - zeroPos;
    }

    /**
     * Checks if a given height is within the lower or upper bound of the elevator.
     * @param ticks the amount of ticks that are going to be added / subtracted from its current position. Positive is up. Negative is down.
     * @return zeroPos if encoder value would be lower than zeroPos, maxPos if encoder value would be higher than maxPos, else returns the desired encoder position.
     */
    public static double checkHeight(double ticks) {
        if(zeroPos + ticks <= zeroPos) {
            return zeroPos;
        } else if(zeroPos + ticks >= maxPos) {
            return maxPos;
        }
        return ticks;
    }

    private static void updateBounds() {
        zeroPos = _elevator.getSelectedSensorPosition(0);
        maxPos = zeroPos + Constants.ELEVATOR_ENCODER_RANGE - 1000; //this is most definitely a magic number
    }

}
