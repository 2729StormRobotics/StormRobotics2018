package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class DriveTrain extends Subsystem{

    public static final TalonSRX _leftMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_MAIN);
    public static final TalonSRX _left2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_2);

    public static final TalonSRX _rightMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    public static final TalonSRX _right2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_2);

    public DriveTrain() {
        _rightMain.setInverted(true);
        _right2.setInverted(true);
        _left2.follow(_leftMain);
        _right2.follow(_rightMain);
    }

    @Override
    protected void initDefaultCommand() {
        
    }

    public static void stormDrive(double combinedSpeed, double turn){
        //Left and Right triggers control speed.  Steer with joystick
        turn = turn * Math.abs(turn);

        int mult;
        if (combinedSpeed < 0)
            mult = -1;
        else
            mult = 1;


        if (Math.abs(turn) > 0.1)
            turn = mult * turn;
        else
            turn = 0;

        double leftSpeed = (combinedSpeed - turn);
        leftSpeed = leftSpeed * Math.abs(leftSpeed);

        double rightSpeed = combinedSpeed + turn;
        rightSpeed = rightSpeed * Math.abs(rightSpeed);

        if(Math.abs(leftSpeed) > 0.05)
            _leftMain.set(ControlMode.PercentOutput, leftSpeed);
        else
            _leftMain.set(ControlMode.PercentOutput, 0);

        if(Math.abs(rightSpeed) > 0.05)
            _rightMain.set(ControlMode.PercentOutput, rightSpeed);
        else
            _rightMain.set(ControlMode.PercentOutput, 0);

    }

    public static void tankDrive(double leftSpeed, double rightSpeed){
        tankDrive(leftSpeed,rightSpeed,false, 0.0);
    }

    public static void tankDrive(double leftSpeed, double rightSpeed, boolean squareValues){
        tankDrive(leftSpeed,rightSpeed,squareValues, 0.0);
    }


    public static void tankDrive(double leftSpeed, double rightSpeed, boolean squareValues, double tolerance){

        if(squareValues){
            leftSpeed = Math.pow(leftSpeed, 2);
            rightSpeed = Math.pow(rightSpeed, 2);
        }

        _leftMain.set(ControlMode.PercentOutput, leftSpeed);
        _rightMain.set(ControlMode.PercentOutput, rightSpeed);

    }

    public void pointTurn(double degrees){

    }

}