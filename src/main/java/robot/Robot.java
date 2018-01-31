package robot;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import AutoModes.Modes.LeftScale;
import AutoModes.Modes.MidSwitch;
import AutoModes.Modes.RightSwitch;
import AutoModes.Modes.TestMode;
import Subsystems.*;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.File;


public class Robot extends IterativeRobot {
    public static File traj;

    //public static DriveTrain driveTrain = new DriveTrain();
    //public static Elevator elevator = new Elevator();
    //public static Hanger hanger = new Hanger();
    public static Intake intake = new Intake();

    private SendableChooser autoChooser;
    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;
    private double elevateSpeed;
    private double pullSpeed;
    private double hookSpeed;
    private int output;
   // private int outputBackward;
    private boolean intakeOn;
    private boolean lowGear;
    public boolean accelerationDisable;
    public boolean autonomousOn;
    boolean listenToA;
    public XboxController xboxDrive;
    public XboxController xboxDrive2;
    public static final NavX navx = new NavX();


    public static final class Auto {
        public static final String MID_SWITCH = "Mid Switch";
        public static final String LEFT_SWITCH = "Left Side Switch";
        public static final String LEFT_SCALE = "Left Side Scale";
        public static final String RIGHT_SWITCH = "Right Switch";
        public static final String POINT_TURN = "Point Turn";
        public static final String MOVE_FORWARD = "Move Forward";
        public static final String TEST_MODE = "Test Mode";
    }

    @Override
    public void robotInit() {
        /*
        Waypoint[] points = new Waypoint[] {
                new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
                new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
        };

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory trajectory = Pathfinder.generate(points, config);

        traj = new File("Trajectory.traj");
        Pathfinder.writeToFile(traj, trajectory);
        */

        xboxDrive = new XboxController(Constants.PORT_XBOX_DRIVE);
        xboxDrive2 = new XboxController(Constants.PORT_XBOX_WEAPONS);

        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Auto.POINT_TURN, new PointTurn(180));
        autoChooser.addObject(Auto.MID_SWITCH, new MidSwitch('R'));
        autoChooser.addObject(Auto.RIGHT_SWITCH, new RightSwitch());
        autoChooser.addObject(Auto.LEFT_SCALE, new LeftScale());
        autoChooser.addObject(Auto.POINT_TURN, new PointTurn(180));
        autoChooser.addObject(Auto.MOVE_FORWARD, new MoveForward(150)); //change distance
        autoChooser.addObject(Auto.TEST_MODE, new TestMode());


        SmartDashboard.putData("Autonomous Modes", autoChooser);
        NavX.getNavx();

        listenToA = true;
        accelerationDisable = false;
        intakeOn = false;
        lowGear = false;
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
        autonomousOn = true;
        Command autonomousCommand = (Command) autoChooser.getSelected();

        if (autonomousCommand != null) {
            System.err.println("Auto " + autoChooser.getSelected() + " selected!");
            autonomousCommand.start();
        } else {
            System.err.println("Auto not selected!");
            //DriveTrain.pointTurn(90);
        }


    }

    @Override
    public void teleopInit() {
        autonomousOn = false;
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
    }

    @Override
    public void teleopPeriodic() {
        DriveTrain.dashboardStats();

        SmartDashboard.putNumber("Left Encoder", DriveTrain._leftMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder", DriveTrain._rightMain.getSelectedSensorPosition(0));

        double intakeL = 0.0;
        intakeL = SmartDashboard.getNumber("Intake Speed", 0.0);
        Intake.fwoo(intakeL);

        double combinedSpeed = forwardSpeed - reverseSpeed;

        //Controller 1: Driver
        turnSpeed = xboxDrive.getX(GenericHID.Hand.kLeft);
        elevateSpeed = xboxDrive.getX(GenericHID.Hand.kRight);
        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);
        lowGear = xboxDrive.getBumper(XboxController.Hand.kLeft);

        //Controller 2
        elevateSpeed = xboxDrive2.getX(GenericHID.Hand.kRight);  //elevator right stick
        pullSpeed = xboxDrive2.getTriggerAxis(XboxController.Hand.kLeft);  //winch left trigger
        intakeOn = xboxDrive2.getBumper(XboxController.Hand.kLeft);  //intake left bumper
        hookSpeed = xboxDrive2.getX(GenericHID.Hand.kLeft);  //hook set lift thing right stick
        output = xboxDrive.getPOV(); //block output d-pad
        //elevator.output(output);



        toggleAcceleration();
        SmartDashboard.putBoolean("Accel Disable", accelerationDisable);


        pullSpeed = xboxDrive2.getTriggerAxis(XboxController.Hand.kRight);

        DriveTrain.stormDrive(combinedSpeed, 0.0, turnSpeed, accelerationDisable);
        Hanger.pull(pullSpeed);
        Elevator.elevate(elevateSpeed, false);

    }

    @Override
    public void testPeriodic() {
        DriveTrain.dashboardStats();
    }

    private void toggleAcceleration(){
        boolean aIsPressed = xboxDrive.getAButtonPressed();
        if(aIsPressed && listenToA){
            accelerationDisable = ! accelerationDisable;
        }
    }
}