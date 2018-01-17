package AutoModes.Commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PointTurn extends Command {
    AHRS ahrs;
    TalonSRX left, right;
    PIDController turnController;
    PIDSource angleSource = new PIDSource() {
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {

        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return null;
        }

        public double pidGet() { // Angle Robot at
            return ahrs.getRawGyroZ();
        }
    };
    PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            left.set(ControlMode.PercentOutput, a);
            right.set(ControlMode.PercentOutput, a); //change to -a later when .setInverted works
        }
    };

    double targetAngle;
    static final double toleranceDegrees = 2.0;

    public PointTurn(AHRS _ahrs, double angle, TalonSRX _left, TalonSRX _right) {
        ahrs = _ahrs;
        left = _left;
        right = _right;
        ahrs.reset();
        targetAngle = angle;
    }

    @Override
    protected void initialize() {
        turnController = new PIDController(0.03, 0.00, 0.00, 0.00, angleSource, motorSpeedWrite);
        turnController.setInputRange(-180.0, 180.0);
        turnController.setOutputRange(-1, 1);
        turnController.setAbsoluteTolerance(toleranceDegrees);
        turnController.setContinuous(true);
        turnController.setSetpoint(targetAngle);
        turnController.enable();
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        if (Math.abs(turnController.getError()) < toleranceDegrees){
            turnController.disable();
            return true;
        }
        return false;
    }
}
