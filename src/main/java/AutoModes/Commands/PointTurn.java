package AutoModes.Commands;

import Subsystems.DriveTrain;
import Subsystems.NavX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Constants;

public class PointTurn extends Command {
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
            SmartDashboard.putBoolean("NavXDemo Connected", NavX.getNavx().isConnected());
            return NavX.getNavx().getYaw();
        }
    };

    PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);

            turnSpeed = a;  //change to -a later when .setInverted works
        }
    };


    public PointTurn(double angle) {
        targetAngle = angle;
    }

    @Override
    public synchronized void start() {
        super.start();
        System.err.println("start Point Turn");
    }

    @Override
    protected void initialize() {
        System.err.println("initialize Point Turn");
        turnController = new PIDController(Constants.TURNCONTROLLER_P, Constants.TURNCONTROLLER_I, Constants.TURNCONTROLLER_D, Constants.TURNCONTROLLER_F, angleSource, motorSpeedWrite, Constants.TURNCONTROLLER_PERIOD);
        turnController.setInputRange(-180.0, 180.0);
        turnController.setOutputRange(-.75, .75);
        turnController.setAbsoluteTolerance(Constants.POINT_TURN_TOLERANCE);
        turnController.setContinuous(true);
        turnController.setSetpoint(targetAngle + NavX.getNavx().getYaw());
        turnController.enable();
        System.err.println("initialize Point Turn");
    }

    @Override
    protected void end() {
        System.err.println("end Point Turn");
        turnController.disable();
        DriveTrain.tankDrive(0, 0);
        super.end();
    }

    @Override
    protected void interrupted() {
        System.err.println("interrupted Point Turn");
        turnController.disable();
        DriveTrain.tankDrive(0, 0);
        super.interrupted();
    }

    @Override
    protected void execute() {
        super.execute();

        DriveTrain._leftMain.set(ControlMode.PercentOutput, turnSpeed);
        DriveTrain._rightMain.set(ControlMode.PercentOutput, -turnSpeed);

        System.err.println("execute Point Turn");
    }


    @Override
    protected boolean isFinished() {
        if (turnController.get() >= -0.01 && turnController.get() <= 0.01 && turnController.onTarget()) {
            turnController.disable();

            DriveTrain.tankDrive(0, 0);
            return true;
        }
        return false;

    }
}
