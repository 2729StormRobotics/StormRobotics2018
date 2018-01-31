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
    double turnSpeed, targetAngle, er;
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
    protected void initialize() {
        super.initialize();
        turnController = new PIDController(
                Constants.TURNCONTROLLER_P,
                Constants.TURNCONTROLLER_I,
                Constants.TURNCONTROLLER_D,
                Constants.TURNCONTROLLER_F,
                angleSource,
                motorSpeedWrite,
                Constants.TURNCONTROLLER_PERIOD);
        turnController.setInputRange(-180.0, 180.0);
        turnController.setOutputRange(-.80, .80);
        turnController.setAbsoluteTolerance(Constants.POINT_TURN_TOLERANCE);
        turnController.setContinuous(true);
        turnController.setSetpoint(targetAngle + NavX.getNavx().getYaw());
        turnController.enable();
        System.err.println("start Point Turn");
    }

    @Override
    protected void end() {
        super.end();
        System.err.println("end Point Turn");
        turnController.disable();
        turnController = null;
        DriveTrain.tankDrive(0, 0);
    }

    @Override
    protected void interrupted() {
        super.interrupted();
        System.err.println("interrupted Point Turn");
        end();
    }

    @Override
    protected void execute() {
        super.execute();

        DriveTrain.tankDrive(turnSpeed, -turnSpeed, false, 0);

        SmartDashboard.putNumber("Error: ", er - NavX.getNavx().getYaw());
    }


    @Override
    protected boolean isFinished() {
        if (Math.abs(turnController.getError()) < Constants.POINT_TURN_TOLERANCE) {
            turnController.disable();

            DriveTrain.tankDrive(0, 0);
            System.err.println("finish Point Turn");
            return true;
        }
        return false;

    }
}
