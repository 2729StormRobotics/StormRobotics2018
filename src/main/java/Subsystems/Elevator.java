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

    /**
     * The Elevator subsystem. Controls vertical movement of elevator and the cart that ejects the cube.
     */
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


    /**
     * Moves elevator at set speed.
     * @param liftSpeed the speed to lift the elevator. Positive moves down. Negative moves up.
     */
    public void elevate(double liftSpeed) {

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

    /**
     * Bang bang controller that gets the elevator to a predestined location.
     * @param height the height that the elevator will go to
     */
    public void bangBangHeight(double height) {
        boolean toMakeSimmerMad = true; //always true b/c simmer always is mad
        double setPoint = height * Constants.ELEVATOR_TICKS_PER_INCH;
        if(_elevator.getSelectedSensorPosition(0) < setPoint) {
            elevate(-1);
        } else if(_elevator.getSelectedSensorPosition(0) > setPoint) {
            elevate(1);
        } else {
            elevate(0);
        }
    }

    /**
     * Changed intake between IN and IDLE state.
     */
    public void toggleOutput(){
        if (state != CubeManipState.IN)
            setOutput(CubeManipState.IN);
        else
            setOutput(CubeManipState.IDLE);

        LEDs.shooting = (state == CubeManipState.OUT);

    }

    /**
     * Sets state of Cart, either IN, REVERSE or OFF
     * @param desiredState CubeManipState.IN moves inward, CubeManipState.OUT moves outward, CubeManipState.IDLE is off
     */
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

    /**
     * Returns height of String pot in inches.
     * @return height of String pot after accounting for horizontal displacement.
     */
    private static double getPotInches() { return convertHypotToY(); }

    /**
     * Returns height of elevator.  Uses string pot if in range.  Otherwise uses encoders.
     * @return height of the y component of the String Pot if below the max String Pot fraction else returns encoder height in inches.
     */
    public static double getHeight() {
        if(pot.get() <= Constants.STRPOT_SWITCH_FRACTION) {
            updateSwitchPos();
            return getPotInches();
        } else {
            return ((_elevator.getSelectedSensorPosition(0) - switchPos) / Constants.ELEVATOR_TICKS_PER_INCH) + getPotInches();
        }
    }

    /**
     * Returns the zero position of the elevator encoder.
     * @return returns the zero position of the elevator encoder.
     */
    public static double getZeroPos() {
        return zeroPos;
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
        //System.out.println(pot.get());
        return pot.get();
    }

    /**
     * Gets fraction of String Pot extended.
     * @return the fraction of String Pot extended.
     */
    public static double getTicks() {
        return _elevator.getSelectedSensorPosition(0);
    }

    /**
     * Checks if a given height is within the lower or upper bound of the elevator.
     * @param ticks the amount of ticks that are going to be added / subtracted from its current position. Positive is up. Negative is down.
     * @return zeroPos if encoder value would be lower than zeroPos, maxPos if encoder value would be higher than maxPos, else returns the desired encoder position.
     */
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

    /**
     * Multiplies the fraction of the String Pot w/ its max length.
     * @return the height of the String Pot relative to its max length.
     */
    private static double getHypotInches() {
        return pot.get() * Constants.STRPOT_MAX;
    }

    private static double convertHypotToY() {
        return Math.sqrt(Math.pow(getHypotInches(), 2) - 1);
    }
}

/* Different approach: Get the fraction of the string pot at starting height and get the max fraction that it extends. If stringPot.get() > than the max value, use encoders*/
