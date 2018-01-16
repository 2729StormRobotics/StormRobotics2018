package AutoModes;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PointTurn extends PIDSubsystem{

    AHRS ahrs;
    TalonSRX left, right;
    double angle;
    PIDController turnController;
    double rotateToAngleRate;

    static final double toleranceDegrees = 2.0;

    public PointTurn() {
        super("PointTurn", 2, 2, 2);
        try {
            ahrs = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex) {
            DriverStation.reportError("Error", true;
        }

        turnController = new PIDController(0.03, 0.00, 0.00, 0.00, ahrs, this::usePIDOutput);
        turnController.setInputRange(-180.0, 180.0);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(toleranceDegrees);
        turnController.setContinuous(true);

        
    }



    @Override
    protected double returnPIDInput() {
        return 0;
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected void usePIDOutput(double output) {

    }
}
