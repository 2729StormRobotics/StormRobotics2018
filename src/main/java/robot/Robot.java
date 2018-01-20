package robot;

import AutoModes.Commands.MoveForward;
import AutoModes.Modes.LeftScale;
import AutoModes.Modes.MidSwitch;
import AutoModes.Commands.PointTurn;
import Subsystems.DriveTrain;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;


public class Robot extends IterativeRobot {
    private AHRS ahrs;

    public static File traj;

    public DriveTrain driveTrain = new DriveTrain();

    private SendableChooser autoChooser;
    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;
    public XboxController xboxDrive;


    public static final class Auto{
        public static final String MID_SWITCH = "Mid Switch";
        public static final String LEFT_SWITCH = "Left Side Switch";
        public static final String LEFT_SCALE = "Left Side Scale";
        public static final String POINT_TURN = "Point Turn";
        public static final String MOVE_FORWARD = "Move Forward";
    }

    @Override
    public void robotInit() {


        try {
            ahrs = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }

        Waypoint[] points = new Waypoint[] {
                new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
                new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
        };

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory trajectory = Pathfinder.generate(points, config);

        traj = new File("Trajectory.traj");
        Pathfinder.writeToFile(traj, trajectory);


        xboxDrive = new XboxController(Constants.PORT_XBOX_DRIVE);

        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Auto.POINT_TURN, new PointTurn(ahrs, 90));
        autoChooser.addObject(Auto.MID_SWITCH, new MidSwitch(DriveTrain._leftMain, DriveTrain._rightMain, ahrs));
        autoChooser.addObject(Auto.LEFT_SCALE, new LeftScale(DriveTrain._leftMain, DriveTrain._rightMain, ahrs));
        autoChooser.addObject(Auto.POINT_TURN, new PointTurn(ahrs, 90));
        autoChooser.addObject(Auto.MOVE_FORWARD, new MoveForward(ahrs, 10, DriveTrain._leftMain, DriveTrain._rightMain)); //change distance

        SmartDashboard.putData("Autonomous Modes", autoChooser);

    }

    @Override
    public void disabledInit() { }

    @Override
    public void autonomousInit() {
        Command autonomousCommand = (Command) autoChooser.getSelected();

        if (autonomousCommand != null) {
            System.err.println("Auto " + autoChooser.getSelected() + " selected!");
            autonomousCommand.start();
        } else {
            System.err.println("Auto not selected!");
            driveTrain.pointTurn(90);
        }



    }

    @Override
    public void teleopInit() { }

    @Override
    public void testInit() { }


    @Override
    public void disabledPeriodic() { }
    
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopPeriodic() {


        double combinedSpeed = forwardSpeed - reverseSpeed;
        turnSpeed = xboxDrive.getX(GenericHID.Hand.kLeft);

        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);

        DriveTrain.stormDrive(combinedSpeed, turnSpeed);

    }

    @Override
    public void testPeriodic() { }
}