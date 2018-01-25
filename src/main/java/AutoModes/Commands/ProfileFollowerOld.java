package AutoModes.Commands;

import Subsystems.DriveTrain;
import Subsystems.NavX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import robot.Robot;

import java.io.File;

public class ProfileFollowerOld extends Command{

    File traj;
    TalonSRX leftMotor, rightMotor;
    EncoderFollower left;
    EncoderFollower right;
    Trajectory leftTra;
    Trajectory rightTra;
    File leftMotionProfile;
    File rightMotionProfile;


    public ProfileFollowerOld(String leftCSV, String rightCSV){


        leftMotionProfile = new File(leftCSV);
        rightMotionProfile = new File(rightCSV);


        leftMotor = DriveTrain._leftMain;
        rightMotor = DriveTrain._rightMain;


        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);

        try {
            Trajectory leftTrajectory = Pathfinder.readFromCSV(leftMotionProfile);
            Trajectory rightTrajectory = Pathfinder.readFromCSV(rightMotionProfile);

            left = new EncoderFollower(leftTrajectory);

            right = new EncoderFollower(rightTrajectory);

            leftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

            left.configureEncoder(leftMotor.getSelectedSensorPosition(0), 1024, 4);

            rightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

            right.configureEncoder(rightMotor.getSelectedSensorPosition(0), 1024, 4);

        } catch (Exception e) {
            System.err.println("ProfileFollower: FAILED TO LOAD \"" + rightCSV + "\" motion profile.");
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
        double gyro_heading = NavX.getNavx().getYaw();
        double desired_heading = Pathfinder.r2d(left.getHeading());
        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;
        System.out.println("Left: " + (l + turn));
        System.out.println("Right: " + (r - turn));
        leftMotor.set(ControlMode.PercentOutput, l + turn);
        rightMotor.set(ControlMode.PercentOutput, -(r - turn));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}