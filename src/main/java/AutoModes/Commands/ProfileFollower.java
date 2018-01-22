package AutoModes.Commands;

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

import java.io.File;

public class ProfileFollower extends Command{

    File traj;
    TalonSRX leftMotor, rightMotor;
    EncoderFollower left;
    EncoderFollower right;
    Trajectory leftTra;
    Trajectory rightTra;
    AHRS navx;
    File motionProfile;


    public ProfileFollower(TalonSRX _left, TalonSRX _right, AHRS navx, String csv){

        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(10, 3, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
                new Waypoint(12, 7, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
        };


        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory trajectory = Pathfinder.generate(points, config);

//        traj = new File("Trajectory.traj");
//        Pathfinder.writeToFile(traj, trajectory);

        this.navx = navx;
//        motionProfile = new File(csv);


        leftMotor = _left;
        rightMotor = _right;


        //Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);

        try {
            //Trajectory trajectory = Pathfinder.readFromCSV(motionProfile);
            TankModifier modifier = new TankModifier(trajectory).modify(0.5);

            left = new EncoderFollower(modifier.getLeftTrajectory());

            right = new EncoderFollower(modifier.getRightTrajectory());

            leftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

            left.configureEncoder(leftMotor.getSelectedSensorPosition(0), 1000, 6);

            rightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

            right.configureEncoder(rightMotor.getSelectedSensorPosition(0), 1000, 6);


            leftTra = modifier.getLeftTrajectory();

            rightTra = modifier.getRightTrajectory();

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
        rightMotor.set(ControlMode.PercentOutput, -(r - turn));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}