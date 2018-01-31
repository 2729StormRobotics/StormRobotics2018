package Subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class Intake extends Subsystem{
    public static TalonSRX _intakeLeft = new TalonSRX(Constants.PORT_MOTOR_INTAKE_LEFT);
    public static TalonSRX _intakeRight = new TalonSRX(Constants.PORT_MOTOR_INTAKE_RIGHT);

    public Intake(){

        // use these to invert motors if needed
        _intakeRight.setInverted(true);
//        _intakeLeft.setInverted(true);

        _intakeRight.follow(_intakeLeft);
    }

    protected void initDefaultCommand() {

    }

    public static void fwoo(double intakeSpeed){
        System.out.println("Intake Speed: " + intakeSpeed);
        _intakeLeft.set(ControlMode.PercentOutput, intakeSpeed);
    }
}
