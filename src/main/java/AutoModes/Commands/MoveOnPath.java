package AutoModes.Commands;


import Subsystems.DriveTrain;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import robot.Constants;


import java.io.File;

/**
 * Use motion profiling to move on a specified path
 */
public class MoveOnPath extends Command {
    private static Logger log = LogManager.getLogger(MoveOnPath.class);
    private TalonSRX left;
    private TalonSRX right;

    private final int TRAJECTORY_SIZE;

    private Trajectory trajectoryR, trajectoryL;

    private MotionProfileStatus statusLeft, statusRight;
    private static Notifier trajectoryProcessor;

    private boolean isRunning;
    private int dir;

    public enum Direction {
        BACKWARD,
        FORWARD
    }

    /**
     * Creates this command using the file prefix to determine
     * the files to load.
     *
     * @param name name of the trajectory
     */
    public MoveOnPath(String name, Direction direction) {

        setName("MoveOnPath-" + name);
        log.info(getName() + " Beginning constructor");

        left = DriveTrain._leftMain;
        right = DriveTrain._rightMain;

        switch(direction) {
            case BACKWARD:
                dir = -1;
                break;
            case FORWARD:
            default:
                dir = 1;
                break;
        }

        trajectoryL = Pathfinder.readFromFile(new File("/home/lvuser/MotionProfiles/" + name + "/_left_detailed.traj"));
        trajectoryR = Pathfinder.readFromFile(new File("/home/lvuser/MotionProfiles/" + name + "/_right_detailed.traj"));
//        trajectoryL = Pathfinder.readFromCSV(new File("/home/lvuser/trajectories/" + name + "_left_detailed.csv"));
//        trajectoryR = Pathfinder.readFromCSV(new File("/home/lvuser/trajectories/" + name + "_right_detailed.csv"));

        if (trajectoryProcessor == null) {
            trajectoryProcessor = new Notifier(() -> {
                left.processMotionProfileBuffer();
                right.processMotionProfileBuffer();
            });
        }

        statusLeft = new MotionProfileStatus();
        statusRight = new MotionProfileStatus();

        if (trajectoryL != null) {
            TRAJECTORY_SIZE = trajectoryL.length();

            log.info(getName() + " construced: " + TRAJECTORY_SIZE);
        } else {
            TRAJECTORY_SIZE = 0;
            log.info(getName() + " could not be constructed!");
            end();
        }
    }

    //Called just before this Command runs for the first time.
    protected void initialize() {
        System.out.println("MoveOnPath: Initializing...");

        // Reset command state
        reset();

        // Configure PID values
//        double[] pid = DriveTrainSettings.getPIDValues("moveOnPath");
//        configurePID(pid[0], pid[1], pid[2], Robot.driveTrain.getFeedForward());

        // Change motion control frame period
        left.changeMotionControlFramePeriod(10);
        right.changeMotionControlFramePeriod(10);

        // Fill TOP (API-level) buffer
        fillTopBuffer();

        // Start processing
        // i.e.: moving API points to RAM
        trajectoryProcessor.startPeriodic(0.005);
        log.info(getName() + " Initialized");
    }

    //Called repeatedly when this Command is scheduled to run.
    protected void execute() {
        left.getMotionProfileStatus(statusLeft);
        right.getMotionProfileStatus(statusRight);

        // Give a slight buffer when we process to make sure we don't bite off more than
        // we can chew or however that metaphor goes.
        if (!isRunning && statusLeft.btmBufferCnt >= 5 && statusRight.btmBufferCnt >= 5) {
            setMotionProfileMode(SetValueMotionProfile.Enable);

            log.log(Level.INFO, "Starting motion profile...");

            isRunning = true;
        }
    }

    @Override
    protected boolean isFinished() {
        // If we're running, only finish if both talons
        // reach their last valid point
        return
            isRunning &&
                statusLeft.activePointValid &&
                statusLeft.isLast &&
                statusRight.activePointValid &&
                statusRight.isLast;
    }

    @Override
    protected void end() {
        // Stop processing trajectories
        trajectoryProcessor.stop();

        left.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, 10);
        right.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, 10);

        right.set(ControlMode.PercentOutput, 0);
        left.set(ControlMode.PercentOutput, 0);

        log.log(Level.INFO, "Finished running");
    }

    /**
     * Fill top-level (API-level) buffer with all points
     */
    private void fillTopBuffer() {
        for (int i = 0; i < TRAJECTORY_SIZE; i++) {
            TrajectoryPoint trajPointL = new TrajectoryPoint();
            TrajectoryPoint trajPointR = new TrajectoryPoint();

            // NOTE: Encoder ticks are backwards, we need to work with that.
            double currentPosL = -trajectoryL.segments[i].position * dir;
            double currentPosR = -trajectoryR.segments[i].position * dir;

            double velocityL = trajectoryL.segments[i].velocity;
            double velocityR = trajectoryR.segments[i].velocity;

            boolean isLastPointL = TRAJECTORY_SIZE == i + 1;
            boolean isLastPointR = TRAJECTORY_SIZE == i + 1;
            boolean isZero = i == 0;

            // For each point, fill our structure and pass it to API
            trajPointL.position = currentPosL / Constants.TICKS_PER_REV * 12.52; //Convert Revolutions to Units
            trajPointR.position = currentPosR / Constants.TICKS_PER_REV * 12.52;
            trajPointL.velocity = velocityL * Constants.TICKS_PER_REV / 600; //Convert RPM to Units/100ms
            trajPointR.velocity = velocityR * Constants.TICKS_PER_REV / 600;
            trajPointL.profileSlotSelect0 = 0;
            trajPointR.profileSlotSelect0 = 0;

            // Sets the duration of each trajectory point to 20ms
            trajPointL.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;
            trajPointR.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;

            // Set these to true on the first point
            trajPointL.zeroPos = isZero;
            trajPointR.zeroPos = isZero;

            // Set these to true on the last point
            trajPointL.isLastPoint = isLastPointL;
            trajPointR.isLastPoint = isLastPointR;

            // Push to API level buffer
            left.pushMotionProfileTrajectory(trajPointL);
            right.pushMotionProfileTrajectory(trajPointR);
        }
    }

    private void configurePID(double p, double i, double d, double f) {
        left.config_kP(0, p, 10);
        right.config_kP(0, p, 10);

        left.config_kI(0, i, 10);
        right.config_kI(0, i, 10);

        left.config_kD(0, d, 10);
        right.config_kD(0, d, 10);

        left.config_kF(0, f, 10);
        right.config_kF(0, f, 10);
    }

    private void setMotionProfileMode(SetValueMotionProfile value) {
        left.set(ControlMode.MotionProfile, value.value);
        right.set(ControlMode.MotionProfile, value.value);
    }

    private void reset() {
        // Reset flags and motion profile modes
        isRunning = false;
        setMotionProfileMode(SetValueMotionProfile.Disable);

        // Clear the trajectory buffer
        left.clearMotionProfileTrajectories();
        right.clearMotionProfileTrajectories();

        log.log(Level.INFO, "Cleared trajectories; check: " + statusLeft.btmBufferCnt);
    }
}
