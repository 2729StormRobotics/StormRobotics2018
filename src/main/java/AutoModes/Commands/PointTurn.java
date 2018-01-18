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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PointTurn extends Command {
    AHRS ahrs;
    TalonSRX left, right;
    double turnSpeed, targetAngle;
    PIDController turnController;

    PIDSource angleSource = new PIDSource() {
        PIDSourceType pidST;
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {

            pidST = pidSource;
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        public double pidGet() { // Angle Robot at
            return ahrs.getYaw();
        }
    };

    PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            turnSpeed = a;  //change to -a later when .setInverted works
        }
    };

    static final double toleranceDegrees = 2.0;

    public PointTurn(AHRS _ahrs, double angle, TalonSRX _left, TalonSRX _right) {
        ahrs = _ahrs;
        left = _left;
        right = _right;
        targetAngle = angle;
    }

    @Override
    public synchronized void start() {
        super.start();
        System.err.println("start Point Turn");
    }

    @Override
    protected void initialize() {
        super.initialize();
        ahrs.reset();
        System.err.println("initialize Point Turn");
        turnController = new PIDController(0.0095, 0.00, 0.00, 0.00, angleSource, motorSpeedWrite, 0.02);
        turnController.setInputRange(-180.0, 180.0);
        turnController.setOutputRange(-.2, 0.2);
        turnController.setAbsoluteTolerance(toleranceDegrees);
        turnController.setContinuous(true);
        turnController.setSetpoint(targetAngle);
        turnController.enable();
        System.err.println("initialize Point Turn");
    }

    @Override
    protected void end() {
        System.err.println("end Point Turn");
        super.end();
    }

    @Override
    protected void interrupted() {
        System.err.println("interrupted Point Turn");
        super.interrupted();
    }

    @Override
    protected void execute() {
        super.execute();

        System.err.println("Speed: " + turnSpeed + " Gyro: " + ahrs.getRawGyroZ() + " Get: " + turnController.get());

        SmartDashboard.putNumber("Gyro Value: ", ahrs.getRawGyroZ());

        left.set(ControlMode.PercentOutput, turnSpeed);
        right.set(ControlMode.PercentOutput, turnSpeed);

        System.err.println("execute Point Turn");
    }

    @Override
    protected boolean isFinished() {
        return false;
        /*
        if (Math.abs(turnController.getError()) < toleranceDegrees){
            turnController.disable();
            return true;
        }
        return false;
        */
    }
}
