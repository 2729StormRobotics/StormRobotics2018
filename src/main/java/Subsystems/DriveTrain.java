package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class DriveTrain extends Subsystem {

    public boolean acceleration = false;

    public static TalonSRX _leftMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_MAIN);
    public static final TalonSRX _left2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_2);

    public static TalonSRX _rightMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    public static final TalonSRX _right2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_2);

    public static Solenoid _gearShift = new Solenoid(Constants.PORT_SOLENOID_GEARSHIFT);
    public static Solenoid _PTO = new Solenoid(Constants.PORT_SOLENOID_PTO);

    public DriveTrain() {
        _rightMain.setInverted(true);
        _right2.setInverted(true);
        _left2.follow(_leftMain);
        _right2.follow(_rightMain);
    }

    @Override
    protected void initDefaultCommand() {

    }

    public void stormDrive(double combinedSpeed, double turn, boolean forceLow) {
        if(_PTO.get()) _PTO.set(false);
        autoShift(combinedSpeed, forceLow);
        stormDrive(combinedSpeed, turn);
    }

    private void stormDrive(double combinedSpeed, double turn) {
        //Left and Right triggers control speed.  Steer with joystick

        turn = turn * Math.abs(turn);

        int mult;
        if (combinedSpeed < 0)
            mult = 1;
        else
            mult = -1;


        if (Math.abs(turn) > Constants.MIN_TURN_SPEED)
            turn = mult * turn;
        else
            turn = 0;

        double leftSpeed = (combinedSpeed - turn);
        leftSpeed = leftSpeed * Math.abs(leftSpeed);

        double rightSpeed = combinedSpeed + turn;
        rightSpeed = rightSpeed * Math.abs(rightSpeed);

        setMotorTolerance(Constants.MOTOR_TOLERANCE_DEFAULT);

        _leftMain.set(ControlMode.PercentOutput, leftSpeed);
        _rightMain.set(ControlMode.PercentOutput, rightSpeed);

        if(this.acceleration) {
            _leftMain.configOpenloopRamp(1, 10000);
            _rightMain.configOpenloopRamp(1, 10000);
        } else {
            _leftMain.configOpenloopRamp(0, 10000);
            _rightMain.configOpenloopRamp(0, 10000);
        }
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        tankDrive(leftSpeed, rightSpeed, false, Constants.MOTOR_TOLERANCE_DEFAULT);
    }

    public void tankDrive(double leftSpeed, double rightSpeed, boolean squareValues) {
        tankDrive(leftSpeed, rightSpeed, squareValues, Constants.MOTOR_TOLERANCE_DEFAULT);
    }


    public void tankDrive(double leftSpeed, double rightSpeed, boolean squareValues, double tolerance) {
        _leftMain.configOpenloopRamp(0, 10000);
        _rightMain.configOpenloopRamp(0, 10000);

        if (squareValues) {
            leftSpeed = Math.pow(leftSpeed, 2);
            rightSpeed = Math.pow(rightSpeed, 2);
        }

        setMotorTolerance(tolerance);

        _leftMain.set(ControlMode.PercentOutput, leftSpeed);
        _rightMain.set(ControlMode.PercentOutput, rightSpeed);

    }

    private void setMotorTolerance(double tolerance) {
        if (tolerance > Constants.MOTOR_TOLERANCE_MAX) {
            tolerance = Constants.MOTOR_TOLERANCE_MAX;
        } else if (tolerance < Constants.MOTOR_TOLERANCE_MIN) {
            tolerance = Constants.MOTOR_TOLERANCE_MIN;
        }

        _leftMain.configNeutralDeadband(tolerance, 500);
        _rightMain.configNeutralDeadband(tolerance, 500);
    }

    private void autoShift(double speed, boolean force) {
        //False means in High Gear && True Means in Low
        if(_gearShift.get() && speed >= Constants.SHIFT_UP) {
            _gearShift.set(false);
        } else if((!_gearShift.get() && speed <= Constants.SHIFT_DOWN) || force) {
            _gearShift.set(true);
        }
    }

    public void hang(double pullSpeed) {
        if(!_PTO.get()) togglePTO();
        _leftMain.set(ControlMode.PercentOutput, pullSpeed);
        _rightMain.set(ControlMode.PercentOutput, pullSpeed);

        LEDs.hanging = pullSpeed > 0;
    }

    private static void togglePTO(){
        _PTO.set(!_PTO.get());
        _gearShift.set(true);
    }


    public void toggleAcceleration(){
        acceleration = ! acceleration;
    }
}
