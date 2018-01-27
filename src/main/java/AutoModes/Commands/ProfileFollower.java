package AutoModes.Commands;

import Subsystems.DriveTrain;
import Subsystems.NavX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

import java.io.File;

public class ProfileFollower extends Command {
    TalonSRX leftMotor, rightMotor;
    EncoderFollower left;
    EncoderFollower right;
    Trajectory leftTra;
    Trajectory rightTra;
    File leftMotionProfile;
    File rightMotionProfile;


    public ProfileFollower(String leftCSV, String rightCSV) {
        leftMotionProfile = new File(leftCSV);
        rightMotionProfile = new File(rightCSV);

        leftMotor = DriveTrain._leftMain;
        rightMotor = DriveTrain._rightMain;
        leftTra = Pathfinder.readFromCSV(leftMotionProfile);
        rightTra = Pathfinder.readFromCSV(rightMotionProfile);

        left = new EncoderFollower(leftTra);
        right = new EncoderFollower(rightTra);

        leftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        left.configureEncoder(leftMotor.getSelectedSensorPosition(0), 1024, 0.1016);

        rightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        right.configureEncoder(rightMotor.getSelectedSensorPosition(0), 1024, 0.1016);

        double max_velocity = 2;
        left.configurePIDVA(1.0, 0.0, 0.5, 1 / max_velocity, 0);
        right.configurePIDVA(1.0, 0.0, 0.5, 1 / max_velocity, 0);
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        super.initialize();
        NavX.getNavx().zeroYaw();
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends,
     * like shutting off a motor that was being used in the command.
     */
    @Override
    protected void end() {
        super.end();
        DriveTrain.tankDrive(0, 0);
    }

    /**
     * Called when the command ends because somebody called {@link Command#cancel() cancel()} or
     * another command shared the same requirements as this one, and booted it out.
     * <p>
     * <p>This is where you may want to wrap up loose ends, like shutting off a motor that was being
     * used in the command.
     * <p>
     * <p>Generally, it is useful to simply call the {@link Command#end() end()} method within this
     * method, as done here.
     */
    @Override
    protected void interrupted() {
        super.interrupted();
        end();
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        super.execute();
        System.err.println("Execute ProfileFollower.");
        double l = left.calculate(leftMotor.getSelectedSensorPosition(0));
        double r = right.calculate(rightMotor.getSelectedSensorPosition(0));
        //double gyro_heading = NavX.getNavx().getYaw();
        //double desired_heading = Pathfinder.r2d(left.getHeading());
        //double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        //double turn = 0.8 * (-1.0/80.0) * angleDifference;
        System.out.println("Left: " + (l));// + turn));
        System.out.println("Right: " + (r));// - turn));
        DriveTrain.tankDrive(l, r, false, 0);
        //leftMotor.set(ControlMode.PercentOutput, l);// + turn);
        //rightMotor.set(ControlMode.PercentOutput, -(r));// - turn));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}