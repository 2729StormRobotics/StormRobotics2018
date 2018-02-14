package Subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import edu.wpi.first.wpilibj.Solenoid;
import robot.Robot;
import util.CubeManipState;
import util.PneumaticsPair;

public class Intake extends Subsystem{
    public static TalonSRX _intakeLeft = new TalonSRX(Constants.PORT_MOTOR_INTAKE_LEFT);
    public static TalonSRX _intakeRight = new TalonSRX(Constants.PORT_MOTOR_INTAKE_RIGHT);
    public static PneumaticsPair sol = new PneumaticsPair(Constants.PORT_SOLENOID_INTAKE_IN, Constants.PORT_SOLENOID_INTAKE_OUT);
    public CubeManipState state;

    public Intake() {
        _intakeRight.setInverted(true);
        _intakeLeft.follow(_intakeRight);
        System.out.println("Reached Intake()");
    }

    public void toggleIntakeArm(){
        sol.set(!sol.get());
        if(!sol.get()){
            LEDs.armsUp = true;
        }
    }

    public void setIntakeArm(boolean _intakeArmOut) {
        sol.set(_intakeArmOut);
    }

    protected void initDefaultCommand() {

    }

    public void setIntake(CubeManipState desiredState){
        if(desiredState == CubeManipState.IN){
            _intakeRight.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED);
            _intakeLeft.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED);
            Robot._elevator.setOutput(CubeManipState.IN);
            state = CubeManipState.IN;
        } else if (desiredState == CubeManipState.OUT) {
            _intakeRight.set(ControlMode.PercentOutput, -Constants.INTAKE_SPEED);
            _intakeLeft.set(ControlMode.PercentOutput, -Constants.INTAKE_SPEED);
            Robot._elevator.setOutput(CubeManipState.IDLE);
            state = CubeManipState.OUT;
        } else if (desiredState == CubeManipState.IDLE){
            _intakeRight.set(ControlMode.PercentOutput, 0);
            _intakeLeft.set(ControlMode.PercentOutput, 0);
            Robot._elevator.setOutput(CubeManipState.IDLE);
            state = CubeManipState.IDLE;
        }
    }
}
