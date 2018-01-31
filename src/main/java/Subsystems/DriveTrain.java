package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Constants;

public class DriveTrain extends Subsystem {

    public static final double MOTOR_TOLERANCE_MAX = 0.25;
    public static final double MOTOR_TOLERANCE_DEFAULT = 0.04;
    public static final double MOTOR_TOLERANCE_MIN = 0.01;

    public static TalonSRX _leftMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_MAIN);
    private static final TalonSRX _left2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_2);

    public static TalonSRX _rightMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    private static final TalonSRX _right2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_2);

    public DriveTrain() {
        _rightMain.setInverted(true);
        _right2.setInverted(true);
        _left2.follow(_leftMain);
        _right2.follow(_rightMain);
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
        DriveTrain.dashboardStats();
    }

    public static void dashboardStats() {
        SmartDashboard.putNumber("Encoder Left", _leftMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Left Velocity", _leftMain.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Encoder Right", _rightMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Right Velocity", _rightMain.getSelectedSensorVelocity(0));

        DriveTrain.dashboardMotorControllerInfo("Motor/right/main/", _rightMain);
        DriveTrain.dashboardMotorControllerInfo("Motor/right/2/", _right2);
        DriveTrain.dashboardMotorControllerInfo("Motor/left/main/", _leftMain);
        DriveTrain.dashboardMotorControllerInfo("Motor/left/2/", _left2);
    }

    private static void dashboardMotorControllerInfo(String category, TalonSRX talon) {
        SmartDashboard.putNumber(category + "Bus Voltage", talon.getBusVoltage());
        SmartDashboard.putNumber(category + "Output Percent", talon.getMotorOutputPercent());
        SmartDashboard.putNumber(category + "Output Voltage", talon.getMotorOutputVoltage());
        SmartDashboard.putNumber(category + "Output Current", talon.getOutputCurrent());
        SmartDashboard.putNumber(category + "Output Watts", talon.getOutputCurrent() * talon.getMotorOutputVoltage());
        SmartDashboard.putString(category + "control Mode", talon.getControlMode().toString());
        SmartDashboard.putNumber(category + "Temperature", talon.getTemperature());
    }

    public static void stormDrive(double combinedSpeed, double acceleration, double turn) {
        stormDrive(combinedSpeed, acceleration, turn, false, MOTOR_TOLERANCE_DEFAULT);
    }

    public static void stormDrive(double combinedSpeed, double acceleration, double turn, boolean accelerationDisable) {
        stormDrive(combinedSpeed, acceleration, turn, accelerationDisable, MOTOR_TOLERANCE_DEFAULT);
    }

    /*
        Double acceleration doesn't seem to do anything, we could probably remove it
     */
    public static void stormDrive(double combinedSpeed, double acceleration, double turn, boolean accelerationDisable, double tolerance) {
        //Left and Right triggers control speed.  Steer with joystick
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
        else{
            _leftMain.configOpenloopRamp(0, 10000);
            _rightMain.configOpenloopRamp(0, 10000);
        }

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

    public void pointTurn(double degrees) {

    }

}