package Subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import robot.Robot;
import util.CubeManipState;

public class Intake extends Subsystem{
    public static TalonSRX _intakeLeft = new TalonSRX(Constants.PORT_MOTOR_INTAKE_LEFT);
    public static TalonSRX _intakeRight = new TalonSRX(Constants.PORT_MOTOR_INTAKE_RIGHT);
    public static DoubleSolenoid sol = new DoubleSolenoid(Constants.PORT_SOLENOID_INTAKE_IN, Constants.PORT_SOLENOID_INTAKE_OUT);
    public CubeManipState state;
    public static DoubleSolenoid.Value armsUp = DoubleSolenoid.Value.kForward;
    public static DoubleSolenoid.Value armsDown = DoubleSolenoid.Value.kReverse;

    /**
     * The intake subsystem.  Controls both intake arms and intake wheels.
     */
    public Intake() {
        _intakeRight.setInverted(true);
        _intakeLeft.follow(_intakeRight);
        System.out.println("Reached Intake()");
    }

    /**
     * Moves arm between up and down state.
     */
    public void toggleIntakeArm(){
        if(sol.get() == armsUp){
            setIntakeArm(false);
        } else {
            setIntakeArm(true);
        }
    }


    /**
     * Sets intake arm to a desired state.
     * @param up true moves arm up, false moves down
     */
    public void setIntakeArm(boolean up) {
        if(up/* && Elevator.getPercentageHeight() > .2*/)
            sol.set(armsUp);
        else
            sol.set(armsDown);
    }

    protected void initDefaultCommand() {

    }

    /**
     * Turns Intake in, reverse or off. If elevator is higher than 60% of max extension, half output speed.
     * @param desiredState CubeManipState.IN moves inward, CubeManipState.OUT moves outward, CubeManipState.IDLE is off
     */
    public void setIntake(CubeManipState desiredState){
        if(desiredState == CubeManipState.IN){
            _intakeRight.set(ControlMode.PercentOutput, -Constants.INTAKE_SPEED);
            _intakeLeft.set(ControlMode.PercentOutput, -Constants.INTAKE_SPEED);
            Robot._elevator.setOutput(CubeManipState.IN);
            state = CubeManipState.IN;
        } else if (desiredState == CubeManipState.OUT) {
            if(Elevator.getPercentageHeight() > 0.6) {
                _intakeRight.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED / 2);
                _intakeLeft.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED / 2);
            } else {
                _intakeRight.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED);
                _intakeLeft.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED);
            }
            Robot._elevator.setOutput(CubeManipState.OUT);
            state = CubeManipState.OUT;
        } else if (desiredState == CubeManipState.IDLE){
            _intakeRight.set(ControlMode.PercentOutput, 0);
            _intakeLeft.set(ControlMode.PercentOutput, 0);
            Robot._elevator.setOutput(CubeManipState.IDLE);
            state = CubeManipState.IDLE;
        }
    }
}
