package Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import util.DriveState;

public class DriveTrain extends Subsystem {

    public boolean acceleration = false;

    public static TalonSRX _leftMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_MAIN);
    public static final TalonSRX _left2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_LEFT_2);

    public static TalonSRX _rightMain = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    public static final TalonSRX _right2 = new TalonSRX(Constants.PORT_MOTOR_DRIVE_RIGHT_2);

    public static DoubleSolenoid _gearShift = new DoubleSolenoid(Constants.PORT_SOLENOID_GEARSHIFT_IN, Constants.PORT_SOLENOID_GEARSHIFT_OUT);
    public static DoubleSolenoid _PTO = new DoubleSolenoid(Constants.PORT_SOLENOID_PTO_IN, Constants.PORT_SOLENOID_PTO_OUT);  //kforward is disengaged
    public DriveState state;
    public static DoubleSolenoid.Value PTOEnabled = DoubleSolenoid.Value.kReverse;
    public static DoubleSolenoid.Value PTODisabled = DoubleSolenoid.Value.kForward;
    public static DoubleSolenoid.Value highGear = DoubleSolenoid.Value.kForward;  //check this
    public static DoubleSolenoid.Value lowGear = DoubleSolenoid.Value.kReverse;  //check this

    public DriveTrain() {
        _leftMain.setInverted(true);
        _left2.setInverted(true);
        _left2.follow(_leftMain);
        _right2.follow(_rightMain);
    }

    @Override
    protected void initDefaultCommand() {

    }

    public void stormDrive(double combinedSpeed, double turn) {  //Left and Right triggers control speed.  Steer with joystick
        autoShift(combinedSpeed);
        turn = turn * Math.abs(turn);

        int mult;
        if (combinedSpeed < 0)
            mult = 1;
        else
            mult = -1;

        if (Math.abs(turn) > Constants.MIN_TURN_SPEED)
            turn = mult * turn;
        else {
            turn = 0;
        }

        double leftSpeed = (combinedSpeed + turn);
        leftSpeed = leftSpeed * Math.abs(leftSpeed);

        double rightSpeed = combinedSpeed - turn;
        rightSpeed = rightSpeed * Math.abs(rightSpeed);

        setMotorTolerance(Constants.MOTOR_TOLERANCE_DEFAULT);

        _leftMain.set(ControlMode.PercentOutput, -leftSpeed);
        _rightMain.set(ControlMode.PercentOutput, -rightSpeed);

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

        _leftMain.set(ControlMode.PercentOutput, -leftSpeed);
        _rightMain.set(ControlMode.PercentOutput, -rightSpeed);

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

    private void autoShift(double speed) { //add back force eventually
        if(_gearShift.get() == lowGear && speed >= Constants.SHIFT_UP) {
            gearShift(true);
        } else if(_gearShift.get() == highGear && speed <= Constants.SHIFT_DOWN) {
            gearShift(false);
        }
    }

    public void hang(double pullSpeed) {
        _leftMain.set(ControlMode.PercentOutput, pullSpeed);
        _rightMain.set(ControlMode.PercentOutput, pullSpeed);

        LEDs.hanging = pullSpeed > 0;
    }

    public void togglePTO(){
        if(_PTO.get() == PTODisabled)
            setPTO(true);
        else if(_PTO.get() == PTOEnabled)
            setPTO(false);
        _gearShift.set(highGear);
    }

    public void setPTO(boolean engaged){
        if(engaged){
            _PTO.set(PTOEnabled);
            state = DriveState.PTO;
        } else {
            _PTO.set(PTODisabled);
            state = DriveState.DRIVE;
        }
        _gearShift.set(highGear);
    }

    public void gearShift(boolean high){
        if(high){
            _gearShift.set(highGear);
        } else {
            _gearShift.set(lowGear);
        }
    }

    public void toggleGear(){
        if(_gearShift.get() == highGear){
            _gearShift.set(lowGear);
        } else {
            _gearShift.set(highGear);
        }
    }


    public void toggleAcceleration(){
        acceleration = ! acceleration;
    }

    public void setAcceleration(boolean enabled){
        acceleration = ! enabled;
    }
}
