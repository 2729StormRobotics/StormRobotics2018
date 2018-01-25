package robot;

import AutoModes.Commands.MoveForward;
import AutoModes.Modes.LeftScale;
import AutoModes.Modes.MidSwitch;
import AutoModes.Commands.PointTurn;
import AutoModes.Modes.RightSwitch;
import Subsystems.DriveTrain;
import Subsystems.Elevator;
import Subsystems.NavX;
import Subsystems.Hanger;
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
    public static File traj;

    public DriveTrain driveTrain = new DriveTrain();
    public Elevator elevator = new Elevator();
    public Hanger hanger = new Hanger();

    private SendableChooser autoChooser;
    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;
    private double elevateSpeed;
    private double pullSpeed;
    public XboxController xboxDrive;
    public XboxController xboxDrive2;
    public static final NavX navx = new NavX();


    public static final class Auto{
        public static final String MID_SWITCH = "Mid Switch";
        public static final String LEFT_SWITCH = "Left Side Switch";
        public static final String LEFT_SCALE = "Left Side Scale";
        public static final String RIGHT_SWITCH = "Right Switch";
        public static final String POINT_TURN = "Point Turn";
        public static final String MOVE_FORWARD = "Move Forward";
    }

    @Override
    public void robotInit() {

        xboxDrive = new XboxController(Constants.PORT_XBOX_DRIVE);
        xboxDrive2 = new XboxController(Constants.PORT_XBOX_WEAPONS);

        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Auto.POINT_TURN, new PointTurn(180));
        autoChooser.addObject(Auto.MID_SWITCH, new MidSwitch());
        autoChooser.addObject(Auto.RIGHT_SWITCH, new RightSwitch());
        autoChooser.addObject(Auto.LEFT_SCALE, new LeftScale());
        autoChooser.addObject(Auto.POINT_TURN, new PointTurn(180));
        autoChooser.addObject(Auto.MOVE_FORWARD, new MoveForward(150)); //change distance

        SmartDashboard.putData("Autonomous Modes", autoChooser);
        NavX.getNavx();
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

        SmartDashboard.putNumber("Left Encoder", DriveTrain._leftMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder", DriveTrain._rightMain.getSelectedSensorPosition(0));



        double combinedSpeed = forwardSpeed - reverseSpeed;
        turnSpeed = xboxDrive.getX(GenericHID.Hand.kLeft);
        elevateSpeed = xboxDrive.getX(GenericHID.Hand.kRight);
        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);

        pullSpeed = xboxDrive2.getTriggerAxis(XboxController.Hand.kRight);

        DriveTrain.stormDrive(combinedSpeed, turnSpeed);
        hanger.pull(pullSpeed);
        elevator.elevate(elevateSpeed, false);

    }

    @Override
    public void testPeriodic() { }
}