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
        System.err.println("ProfileFollower.");
        this.navx = navx;
        motionProfile = new File(csv);
        System.err.println("ProfileFollower: Set file.");
        System.err.println("Can read \"" + csv + "\": " + motionProfile.canRead());
        System.err.println("\"" + csv + "\" exists: " + motionProfile.exists());
        System.err.println("\"" + csv + "\" is file: " + motionProfile.isFile());
        System.err.println("\"" + csv + "\" is directory: " + motionProfile.isDirectory());
        System.err.println("\"" + csv + "\" is hidden: " + motionProfile.isHidden());
        System.err.println("\"" + csv + "\" is absolute: " + motionProfile.isAbsolute());

        leftMotor = _left;
        rightMotor = _right;

        System.err.println("ProfileFollower: Trajectory.Config config.");
        //Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        System.err.println("ProfileFollower: Config.");
        try {
            System.err.println("ProfileFollower: Attempting to read \"" + csv + "\". (\"" + motionProfile.getAbsolutePath() + "\")");
            Trajectory trajectory = Pathfinder.readFromCSV(motionProfile);
            System.err.println("ProfileFollower: Loaded \"" + csv + "\" motion profile.");
            TankModifier modifier = new TankModifier(trajectory).modify(0.5);
            System.err.println("ProfileFollower: Modified trajectory.");
            left = new EncoderFollower(modifier.getLeftTrajectory());
            System.err.println("ProfileFollower: Set left EncoderFollower.");
            right = new EncoderFollower(modifier.getRightTrajectory());
            System.err.println("ProfileFollower: Set right EncoderFollower.");
            leftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
            System.err.println("ProfileFollower: Set leftMotor feedback sensor.");
            left.configureEncoder(leftMotor.getSelectedSensorPosition(0), 1000, 6);
            System.err.println("ProfileFollower: Set left encoder.");
            rightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
            System.err.println("ProfileFollower: Set rightMotor feedback sensor.");
            right.configureEncoder(rightMotor.getSelectedSensorPosition(0), 1000, 6);
            System.err.println("ProfileFollower: Set right encoder.");

            leftTra = modifier.getLeftTrajectory();
            System.err.println("ProfileFollower: Get left trajectory.");
            rightTra = modifier.getRightTrajectory();
            System.err.println("ProfileFollower: Get right trajectory.");
        } catch (Exception e) {
            System.err.println("ProfileFollower: FAILED TO LOAD \"" + csv + "\" motion profile.");
        }



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
        double gyro_heading = navx.getRawGyroZ();
        double desired_heading = Pathfinder.r2d(left.getHeading());
        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;
        System.out.println("Left: " + (l + turn));
        System.out.println("Right: " + (r - turn));
        leftMotor.set(ControlMode.PercentOutput, l + turn);
        rightMotor.set(ControlMode.PercentOutput, r - turn);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}