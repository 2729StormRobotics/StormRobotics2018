package AutoModes.Commands;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

import java.io.File;

public class ProfileFollower extends Command{

    File motionProfile;
    TalonSRX leftMotor, rightMotor;


    public ProfileFollower(String csv){
        motionProfile = new File(csv);

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory trajectory = Pathfinder.readFromCSV(motionProfile);

        TankModifier modifier = new TankModifier(trajectory).modify(0.5);
        EncoderFollower left = new EncoderFollower(modifier.getLeftTrajectory());
        EncoderFollower right = new EncoderFollower(modifier.getRightTrajectory());
        left.configureEncoder(encoder_position, 1000, wheel_diameter);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}