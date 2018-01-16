package AutoModes;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PointTurn extends PIDSubsystem {

    AHRS ahrs;
    TalonSRX left, right;
    PIDController turnController;
    double rotateToAngleRate, targetAngle;

    static final double toleranceDegrees = 2.0;

    public PointTurn(AHRS _ahrs, double agnle) {
        super("PointTurn", 2, 2, 2);
        ahrs = _ahrs;

        ahrs.reset();
        targetAngle = agnle;
        turnController = new PIDController(0.03, 0.00, 0.00, 0.00, ahrs, this::usePIDOutput);
        turnController.setInputRange(-180.0, 180.0);
        turnController.setOutputRange(-400, 400);
        turnController.setAbsoluteTolerance(toleranceDegrees);
        turnController.setContinuous(true);
        turnController.setSetpoint(targetAngle);
    }


    @Override
    protected double returnPIDInput() {

        return ahrs.getRawGyroZ();
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected void usePIDOutput(double output) {
        rotateToAngleRate = output;
        left.set(ControlMode.Velocity, rotateToAngleRate);
        right.set(ControlMode.Velocity, rotateToAngleRate * -1);
    }
}
