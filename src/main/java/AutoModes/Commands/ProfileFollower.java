package AutoModes.Commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import robot.Drives;
import robot.Robot;

import java.io.File;

public class ProfileFollower extends Command{

    File motionProfile;
    TalonSRX leftMotor, rightMotor;
    EncoderFollower left;
    EncoderFollower right;
    Trajectory leftTra;
    Trajectory rightTra;
    AHRS navx;


    public ProfileFollower(TalonSRX _left, TalonSRX _right, AHRS navx, String csv){
        this.navx = navx;
        motionProfile = new File(csv);

        leftMotor = _left;
        rightMotor = _right;

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory trajectory = Pathfinder.readFromCSV(motionProfile);

        TankModifier modifier = new TankModifier(trajectory).modify(0.5);
        left = new EncoderFollower(modifier.getLeftTrajectory());
        right = new EncoderFollower(modifier.getRightTrajectory());
        leftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        left.configureEncoder(leftMotor.getSelectedSensorPosition(0), 1000, 6);
        rightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        right.configureEncoder(rightMotor.getSelectedSensorPosition(0), 1000, 6);

        leftTra = modifier.getLeftTrajectory();
        rightTra = modifier.getRightTrajectory();

    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        super.execute();
        double l = left.calculate(leftMotor.getSelectedSensorPosition(0));
        double r = right.calculate(rightMotor.getSelectedSensorPosition(0));
        double gyro_heading = navx.getRawGyroZ();
        double desired_heading = Pathfinder.r2d(left.getHeading());
        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;
        leftMotor.set(ControlMode.PercentOutput, l + turn);
        rightMotor.set(ControlMode.PercentOutput, r - turn);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}