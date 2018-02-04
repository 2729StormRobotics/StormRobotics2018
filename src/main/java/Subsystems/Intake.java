package Subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake extends Subsystem{
    public static TalonSRX _intakeLeft = new TalonSRX(Constants.PORT_MOTOR_INTAKE_LEFT);
    public static TalonSRX _intakeRight = new TalonSRX(Constants.PORT_MOTOR_INTAKE_RIGHT);


    public static Solenoid sol;

    public Intake(){
        sol = new Solenoid(Constants.PORT_SOLENOID_INTAKE);
        _intakeRight.setInverted(true);
        _intakeRight.follow(_intakeLeft);
    }

    public static void intakeUpDown(boolean pneumaticStatus){
        sol.set(pneumaticStatus);
    }

    protected void initDefaultCommand() {

    }

    public static void fwoo(double intakeSpeed){
        _intakeLeft.set(ControlMode.PercentOutput, intakeSpeed);
    }
}
