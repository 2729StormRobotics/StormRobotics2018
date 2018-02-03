package robot;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import AutoModes.Modes.*;
import Subsystems.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.AutoPosition;
import util.AutoPreference;

import java.io.File;


public class Robot extends IterativeRobot {
    public static File traj;

    public static DriveTrain driveTrain = new DriveTrain();
    public static Elevator elevator = new Elevator();
    public static Hanger hanger = new Hanger();
    public static NavX navx = new NavX();
    public static Intake intake = new Intake();


    private SendableChooser autoChooser;
    private SendableChooser positionChooser;
    private SendableChooser preferenceChooser;

    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;
    private double elevateSpeed;
    private double pullSpeed;
    public boolean accelerationDisable = false;
    public XboxController xboxDrive;
    public XboxController xboxDrive2;

    @Override
    public void robotInit() {
        xboxDrive = new XboxController(Constants.PORT_XBOX_DRIVE);
        xboxDrive2 = new XboxController(Constants.PORT_XBOX_WEAPONS);

        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Constants.POINT_TURN, new PointTurn(90));
        autoChooser.addObject(Constants.MID_SWITCH, new MidSwitch('L'));
        autoChooser.addObject(Constants.RIGHT_SWITCH, new RightSwitch());
        autoChooser.addObject(Constants.LEFT_SCALE, new LeftScale());
        autoChooser.addObject(Constants.RIGHT_SCALE, new RightScale());
        autoChooser.addObject(Constants.POINT_TURN, new PointTurn(90));
        autoChooser.addObject(Constants.MOVE_FORWARD, new MoveForward(262)); //change distance
        autoChooser.addObject(Constants.TEST_MODE, new TestMode());
        autoChooser.addObject(Constants.FOLLOW_PREF, new DummyCommand());

        positionChooser = new SendableChooser<AutoPosition>();
        autoChooser.addDefault(AutoPosition.MIDDLE.getName(), AutoPosition.MIDDLE);
        autoChooser.addObject(AutoPosition.LEFT.getName(), AutoPosition.LEFT);
        autoChooser.addObject(AutoPosition.MIDDLE.getName(), AutoPosition.MIDDLE);
        autoChooser.addObject(AutoPosition.RIGHT.getName(), AutoPosition.RIGHT);

        preferenceChooser = new SendableChooser<AutoPreference>();
        preferenceChooser.addDefault(AutoPreference.SWITCH.getName(), AutoPreference.SWITCH);
        preferenceChooser.addObject(AutoPreference.SWITCH.getName(), AutoPreference.SWITCH);
        preferenceChooser.addObject(AutoPreference.SCALE.getName(), AutoPreference.SCALE);

        SmartDashboard.putData("Test Autonomous Modes", autoChooser);
        SmartDashboard.putData("Auto Position", positionChooser);
        SmartDashboard.putData("Auto Preference", positionChooser);

        NavX.getNavx();

    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        char switchSide = gameData.charAt(0);
        char scaleSide = gameData.charAt(1);

        Command autonomousCommand = (Command) autoChooser.getSelected();
        AutoPosition position = (AutoPosition) positionChooser.getSelected();
        AutoPreference preference = (AutoPreference) preferenceChooser.getSelected();


        if (!autonomousCommand.getName().equalsIgnoreCase(Constants.FOLLOW_PREF)) {
            System.err.println("Auto " + autoChooser.getSelected() + " selected!");
            autonomousCommand.start();
            return;
        }

        switch (position + "-" + preference) {
            case "Left-Scale":
                autonomousCommand = new LeftScale();
                break;
            case "Left-Switch":
                //autonomousCommand = new LeftSwitch(); (Didn't make Left Switch yet)
                break;
            case "Mid-Switch":
                autonomousCommand = new MidSwitch(switchSide);
                break;
            case "Right-Scale":
                autonomousCommand = new RightScale();
                break;
            case "Right-Switch":
                autonomousCommand = new RightSwitch();
                break;
            default: break;
        }

        autonomousCommand.start();


    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void testInit() {
    }


    @Override
    public void disabledPeriodic() {
        DriveTrain.dashboardStats();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        DriveTrain.dashboardStats();
        NavX.dashboardStats();
    }

    @Override
    public void teleopPeriodic() {
        DriveTrain.dashboardStats();
        NavX.dashboardStats();

        SmartDashboard.putNumber("Left Encoder", DriveTrain._leftMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder", DriveTrain._rightMain.getSelectedSensorPosition(0));

        double combinedSpeed = forwardSpeed - reverseSpeed;
        turnSpeed = xboxDrive.getX(GenericHID.Hand.kLeft);
        elevateSpeed = xboxDrive.getX(GenericHID.Hand.kRight);
        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);

        if(xboxDrive.getAButtonPressed()){
            if(!accelerationDisable){
                accelerationDisable = true;
            }
            else{
                accelerationDisable = false;
            }
        }

        SmartDashboard.putBoolean("Accel Disable", accelerationDisable);

        pullSpeed = xboxDrive2.getTriggerAxis(XboxController.Hand.kRight);

        DriveTrain.stormDrive(combinedSpeed, 0.0, turnSpeed, accelerationDisable);
        hanger.pull(pullSpeed);
        elevator.elevate(elevateSpeed, false);
    }

    @Override
    public void testPeriodic() {
        DriveTrain.dashboardStats();
    }
}
