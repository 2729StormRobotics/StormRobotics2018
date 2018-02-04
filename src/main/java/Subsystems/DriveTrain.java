package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class DriveTrain extends Subsystem {

    public static final double MOTOR_TOLERANCE_MAX = 0.25;
    public static final double MOTOR_TOLERANCE_DEFAULT = 0.04;
    public static final double MOTOR_TOLERANCE_MIN = 0.01;

    public static boolean high;

    public static TalonSRX _leftMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_MAIN);
    public static final TalonSRX _left2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_2);

    public static TalonSRX _rightMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    public static final TalonSRX _right2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_2);

    public static Solenoid _gearShift = new Solenoid(Constants.PORT_SOLENOID_GEARSHIFT);
    public static Solenoid _PTO = new Solenoid(0); //set a pto port constant

    public DriveTrain() {
        _rightMain.setInverted(true);
        _right2.setInverted(true);
        _left2.follow(_leftMain);
        _right2.follow(_rightMain);
        high = false;
        //_gearShift.set(false);
    }

    @Override
    protected void initDefaultCommand() {

    }

    /**
     * When the run method of the scheduler is called this method will be called.
     */
    @Override
    public void periodic() {
        super.periodic();
    }

    public static void stormDrive(double combinedSpeed, double acceleration, double turn) {
        stormDrive(combinedSpeed, acceleration, turn, false, MOTOR_TOLERANCE_DEFAULT);
    }

    public static void stormDrive(double combinedSpeed, double acceleration, double turn, boolean accelerationDisable) {
        stormDrive(combinedSpeed, acceleration, turn, accelerationDisable, MOTOR_TOLERANCE_DEFAULT);
    }
    public static void stormDrive(double combinedSpeed, double acceleration, double turn, boolean accelerationDisable, boolean forceLow) {
//        _PTO.set(false);
        combinedSpeed = shift(combinedSpeed, forceLow);
        stormDrive(combinedSpeed, acceleration, turn, accelerationDisable, MOTOR_TOLERANCE_DEFAULT);
    }

    /*
        Double acceleration doesn't seem to do anything, we could probably remove it
     */
    public static void stormDrive(double combinedSpeed, double acceleration, double turn, boolean accelerationDisable, double tolerance) {
        //Left and Right triggers control speed.  Steer with joystick

        combinedSpeed = shift(combinedSpeed, false);

        turn = turn * Math.abs(turn);

        int mult;
        if (combinedSpeed < 0)
            mult = 1;
        else
            mult = -1;


        if (Math.abs(turn) > 0.1)
            turn = mult * turn;
        else
            turn = 0;

        double leftSpeed = (combinedSpeed - turn);
        leftSpeed = leftSpeed * Math.abs(leftSpeed);

        double rightSpeed = combinedSpeed + turn;
        rightSpeed = rightSpeed * Math.abs(rightSpeed);

        DriveTrain.setMotorTolerance(tolerance);

        _leftMain.set(ControlMode.PercentOutput, leftSpeed);
        _rightMain.set(ControlMode.PercentOutput, rightSpeed);

       // System.out.println(accelerationDisable);
        if(!accelerationDisable) {
            _leftMain.configOpenloopRamp(1, 10000);
            _rightMain.configOpenloopRamp(1, 10000);
        } else {
            _leftMain.configOpenloopRamp(0, 10000);
            _rightMain.configOpenloopRamp(0, 10000);
        }
    }

    public static void stormDrive(double combinedSpeed, double acceleration, double turn, boolean accelerationDisable, double tolerance, boolean forceLow) {

    }

    public static void tankDrive(double leftSpeed, double rightSpeed) {
        tankDrive(leftSpeed, rightSpeed, false, MOTOR_TOLERANCE_DEFAULT);
    }

    public static void tankDrive(double leftSpeed, double rightSpeed, boolean squareValues) {
        tankDrive(leftSpeed, rightSpeed, squareValues, MOTOR_TOLERANCE_DEFAULT);
    }


    public static void tankDrive(double leftSpeed, double rightSpeed, boolean squareValues, double tolerance) {
        _leftMain.configOpenloopRamp(0, 10000);
        _rightMain.configOpenloopRamp(0, 10000);

        if (squareValues) {
            leftSpeed = Math.pow(leftSpeed, 2);
            rightSpeed = Math.pow(rightSpeed, 2);
        }

        DriveTrain.setMotorTolerance(tolerance);

        _leftMain.set(ControlMode.PercentOutput, leftSpeed);
        _rightMain.set(ControlMode.PercentOutput, rightSpeed);

    }

    private static void setMotorTolerance(double tolerance) {
        if (tolerance > MOTOR_TOLERANCE_MAX) {
            tolerance = MOTOR_TOLERANCE_MAX;
        } else if (tolerance < MOTOR_TOLERANCE_MIN) {
            tolerance = MOTOR_TOLERANCE_MIN;
        }

        _leftMain.configNeutralDeadband(tolerance, 500);
        _rightMain.configNeutralDeadband(tolerance, 500);
    }

    public static double shift(double speed, boolean forceLow) {
        boolean foo = high;
        if (forceLow) foo = false;

        if(speed <= Constants.SHIFT_DOWN || forceLow){
            speed *= Constants.SHIFT_DOWN_MULT;
        }
        else if(speed >= Constants.SHIFT_UP){
            speed *= Constants.SHIFT_UP_MULT;
        }

        _gearShift.set(foo);
        return speed;

    }
    public static void hang(double pullSpeed) {
        _PTO.set(true);
        _leftMain.set(ControlMode.PercentOutput, pullSpeed);
        _rightMain.set(ControlMode.PercentOutput, pullSpeed);
        if(pullSpeed > 0) {
            LEDs.hanging = true;
        } else {
            LEDs.hanging = false;
        }
    }
}
