package robot;

import com.ctre.CANTalon;

public class Drives {
    CANTalon leftMotor, rightMotor;

    public Drives(CANTalon _left, CANTalon _right){
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

        double right = combinedSpeed + turn;
        right = right * Math.abs(right);

        if(Math.abs(leftSpeed) > 0.05)
            leftMotor.set(leftSpeed);
        else
            leftMotor.set(0);

        if(Math.abs(right) > 0.05)
            rightMotor.set(right);
        else
            rightMotor.set(0);


    }
}