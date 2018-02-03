package Subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import java.awt.*;
public class Intake extends Subsystem{
    public static TalonSRX _intakeLeft = new TalonSRX(Constants.PORT_MOTOR_INTAKE_LEFT);
    public static TalonSRX _intakeRight = new TalonSRX(Constants.PORT_MOTOR_INTAKE_RIGHT);

    //  private boolean pneumaticStatus;


//    Compressor cLeft = new Compressor();
//    Compressor cRight = new Compressor();

    public static Solenoid sol;

    public Intake(){
        sol = new Solenoid(Constants.PORT_SOLENOID_INTAKE);
        // use these to invert motors if needed
        _intakeRight.setInverted(true);
//        _intakeLeft.setInverted(true);

        _intakeRight.follow(_intakeLeft);
        //        cLeft.setClosedLoopControl(true);
//        cRight.setClosedLoopControl(true);
        //  pneumaticStatus = false;
    }

    public static void intakeUpDown(boolean pneumaticStatus){
        sol.set(pneumaticStatus);
    }

    protected void initDefaultCommand() {

    }

    public static void fwoo(double intakeSpeed){
        //System.out.println("Intake Speed: " + intakeSpeed);
        _intakeLeft.set(ControlMode.PercentOutput, intakeSpeed);
    }
}
