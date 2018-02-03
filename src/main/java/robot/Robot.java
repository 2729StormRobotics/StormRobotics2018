package robot;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import AutoModes.Modes.*;
import Subsystems.*;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import org.opencv.core.Mat;
import util.AutoPosition;
import util.AutoPreference;
import util.DebugLevel;

import Subsystems.*;
import java.rmi.Remote;
import java.io.File;


public class Robot extends IterativeRobot {
    public static File traj;

    public static DriveTrain driveTrain = new DriveTrain();
    public static Elevator elevator = new Elevator();
    public static Hanger hanger = new Hanger();
    public static NavX navx = new NavX();
    public static Intake intake = new Intake();


    public static SendableChooser autoChooser;
    public static SendableChooser positionChooser;
    public static SendableChooser preferenceChooser;
    public static SendableChooser debugChooser;

    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;
    private double elevateSpeed;
    private double pullSpeed;
    private DebugLevel bug;

    private double hookSpeed;
    public static boolean accelerationDisable = false;
    private int output;
    private boolean intakeOn;
    private boolean forceLowGear;
    public static boolean armControl;
    public boolean autonomousOn;
    boolean listenToA;
    public XboxController xboxDrive;
    public XboxController xboxDrive2;

    @Override
    public void robotInit() {
        xboxDrive = new XboxController(Constants.PORT_XBOX_DRIVE);
        xboxDrive2 = new XboxController(Constants.PORT_XBOX_WEAPONS);

        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Constants.POINT_TURN, new PointTurn(90));
        autoChooser.addObject(Constants.MID_SWITCH, new MidSwitch('L'));
        autoChooser.addObject(Constants.LEFT_SWITCH, new LeftSwitch());
        autoChooser.addObject(Constants.RIGHT_SWITCH, new RightSwitch());
        autoChooser.addObject(Constants.LEFT_SCALE, new LeftScale());
        autoChooser.addObject(Constants.RIGHT_SCALE, new RightScale());
        autoChooser.addObject(Constants.POINT_TURN, new PointTurn(90));
        autoChooser.addObject(Constants.MOVE_FORWARD, new MoveForward(262)); //change distance
        autoChooser.addObject(Constants.TEST_MODE, new TestMode());
        autoChooser.addObject(Constants.FOLLOW_PREF, new DummyCommand());

        positionChooser = new SendableChooser<AutoPosition>();
        positionChooser.addDefault(AutoPosition.MIDDLE.getName(), AutoPosition.MIDDLE);
        positionChooser.addObject(AutoPosition.LEFT.getName(), AutoPosition.LEFT);
        positionChooser.addObject(AutoPosition.MIDDLE.getName(), AutoPosition.MIDDLE);
        positionChooser.addObject(AutoPosition.RIGHT.getName(), AutoPosition.RIGHT);

        preferenceChooser = new SendableChooser<AutoPreference>();
        preferenceChooser.addDefault(AutoPreference.SWITCH.getName(), AutoPreference.SWITCH);
        preferenceChooser.addObject(AutoPreference.SWITCH.getName(), AutoPreference.SWITCH);
        preferenceChooser.addObject(AutoPreference.SCALE.getName(), AutoPreference.SCALE);

        debugChooser = new SendableChooser<>();
        debugChooser.addDefault(DebugLevel.INFO.getName(), DebugLevel.INFO);
        debugChooser.addObject(DebugLevel.INFO.getName(), DebugLevel.INFO);
        debugChooser.addObject(DebugLevel.DEBUG.getName(), DebugLevel.DEBUG);
        debugChooser.addObject(DebugLevel.ALL.getName(), DebugLevel.ALL);

        Dashboard.sendChooser();
        cameraInit();
        NavX.getNavx();
        listenToA = true;
        accelerationDisable = false;
        intakeOn = false;
        forceLowGear = false;
        armControl = false;
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
        bug = (DebugLevel) debugChooser.getSelected();

        System.out.println(autonomousCommand.getName());

        if (!autonomousCommand.getName().equalsIgnoreCase("DummyCommand")) {
            System.err.println("Auto " + autoChooser.getSelected() + " selected!");
            autonomousCommand.start();
            return;
        }

        switch (position.getName() + "-" + preference.getName()) {
            case "Left-Scale":
                autonomousCommand = new LeftScale();
                break;
            case "Left-Switch":
                autonomousCommand = new LeftSwitch();
                break;
            case "Middle-Switch":
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

        checkBug();
        autonomousCommand.start();
        System.out.println("Running" + autonomousCommand.getName());

    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        checkBug();
        NavX.dashboardStats();
    }

    @Override
    public void teleopPeriodic() {
        NavX.dashboardStats();

        checkBug();

        double combinedSpeed = forwardSpeed - reverseSpeed;
        turnSpeed = xboxDrive.getX(GenericHID.Hand.kLeft);
        elevateSpeed = xboxDrive.getX(GenericHID.Hand.kRight);
        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);
        forceLowGear = xboxDrive.getBumper(XboxController.Hand.kLeft);

        //Controller 2
        elevateSpeed = xboxDrive2.getX(GenericHID.Hand.kRight);  //elevator right stick
        pullSpeed = xboxDrive2.getTriggerAxis(XboxController.Hand.kLeft);  //winch left trigger
        intakeOn = xboxDrive2.getBumper(XboxController.Hand.kLeft);  //intake left bumper
        hookSpeed = xboxDrive2.getX(GenericHID.Hand.kLeft);  //hook set lift thing right stick
        output = xboxDrive.getPOV(); //block output d-pad
        pullSpeed = xboxDrive2.getTriggerAxis(XboxController.Hand.kRight);
        //elevator.output(output);   THIS WILL BE NEEDED LATER DO NOT DELETE

        if (xboxDrive.getXButtonPressed() || xboxDrive2.getXButtonPressed()) {
            System.out.println("Doubt");
            if (xboxDrive.getAButtonPressed()) {
                if (!accelerationDisable) {
                    accelerationDisable = true;
                } else {
                    accelerationDisable = false;
                }
            }
            pullSpeed = xboxDrive2.getTriggerAxis(XboxController.Hand.kRight);
            toggleAcceleration();
            togglePneumatics();
            DriveTrain.stormDrive(combinedSpeed, 0.0, turnSpeed, accelerationDisable);
            hanger.pull(pullSpeed);
            elevator.elevate(elevateSpeed, false);
            Intake.fwoo(Constants.INTAKE_SPEED);
            Hanger.pull(pullSpeed);
            Elevator.elevate(elevateSpeed, false);
            Intake.intakeUpDown(armControl);
            DriveTrain.shift(forceLowGear);
        }



    }

    public void cameraInit() {
        new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(640, 480);

            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("FirstP", 640, 480);

            Mat source = new Mat();
            Mat output = new Mat();

            while (!Thread.interrupted()) {
                cvSink.grabFrame(source);
                outputStream.putFrame(output);
            }
        }).start();
    }

    public void checkBug() {
        switch(bug.getName()) {
            case "Info":
                Dashboard.sendEncoders();
                Dashboard.sendNavXInfo();
                break;
            case "Debug":
                Dashboard.sendEncoders();
                Dashboard.sendElevatorEncoders();
                Dashboard.checkAccel();
                Dashboard.checkPneumatics();
                Dashboard.sendMotorControllerInfo("Motor/right/main/", DriveTrain._rightMain);
                Dashboard.sendMotorControllerInfo("Motor/left/main/", DriveTrain._leftMain);
                break;
            case "All":
                Dashboard.sendEncoders();
                Dashboard.sendElevatorEncoders();
                Dashboard.sendNavXAll();
                Dashboard.checkAccel();
                Dashboard.checkPneumatics();
                Dashboard.checkTurnSpeed();
                Dashboard.sendMotorControllerInfo("Motor/right/main/", DriveTrain._rightMain);
                Dashboard.sendMotorControllerInfo("Motor/right/2/", DriveTrain._right2);
                Dashboard.sendMotorControllerInfo("Motor/left/main/", DriveTrain._leftMain);
                Dashboard.sendMotorControllerInfo("Motor/left/2/", DriveTrain._left2);
                break;
            default: break;
        }
    }

    private void toggleAcceleration(){
        boolean aIsPressed = xboxDrive.getAButtonPressed();
        if(aIsPressed /*&& listenToA*/){
            accelerationDisable = ! accelerationDisable;
        }
    }
    private void togglePneumatics(){
        if(xboxDrive2.getYButtonPressed()) {
            armControl = !armControl;
        }



    }

    }

