package Subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake extends Subsystem{
    private static TalonSRX _intakeLeft = new TalonSRX(Constants.PORT_MOTOR_INTAKE_LEFT);
    private static TalonSRX _intakeRight = new TalonSRX(Constants.PORT_MOTOR_INTAKE_RIGHT);
    private boolean intakeArmOut;

    private static Solenoid sol;

    public Intake(){
        sol = new Solenoid(Constants.PORT_SOLENOID_INTAKE);
        intakeArmOut = sol.get();
        _intakeRight.setInverted(true);
        _intakeRight.follow(_intakeLeft);
    }

    public void toggleIntakeArm(){
        sol.set(!sol.get());
    }

    public void setIntakeArm(boolean _intakeArmOut) {
        sol.set(_intakeArmOut);
    }

    protected void initDefaultCommand() {

    }

    public void fwoo(double intakeSpeed){
        _intakeLeft.set(ControlMode.PercentOutput, intakeSpeed);
    }
}
