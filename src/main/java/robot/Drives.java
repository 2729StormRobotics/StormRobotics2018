package robot;

import com.ctre.CANTalon;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Drives {
    TalonSRX leftMotor, rightMotor;

    public Drives(TalonSRX _left, TalonSRX _right){
        leftMotor = _left;
        rightMotor = _right;
    }

    public void stormDrive(double combinedSpeed, double turn, boolean squareValues){
        //Left and Right triggers control speed.  Steer with joystick
        turn = turn * Math.abs(turn);

        int mult = -1;
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

        if(Math.abs(leftSpeed) > 0.05)
            leftMotor.set(ControlMode.PercentOutput, leftSpeed);
        else
            leftMotor.set(ControlMode.PercentOutput, 0);

        if(Math.abs(rightSpeed) > 0.05)
            rightMotor.set(ControlMode.PercentOutput, rightSpeed);
        else
            rightMotor.set(ControlMode.PercentOutput, 0);


    }
}