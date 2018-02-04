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

public class Robot extends IterativeRobot {

    public static DriveTrain driveTrain = new DriveTrain();
    public static Elevator _elevator = new Elevator();
    public static Hanger _hanger = new Hanger();
    public static NavX navx = new NavX();
    public static Intake _intake = new Intake();

    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;
    private double elevateSpeed;
    private double pullSpeed;
    private DebugLevel bug;

    private double hookSetSpeed;

    public static boolean accelerationDisable = false;
    private int output;
    private boolean intakeOn;
    private boolean forceLowGear;
    public static boolean armControl;
    public boolean autonomousOn;
    boolean listenToA;
    public static boolean readyToHang;
    public XboxController xboxDrive;
    public XboxController xboxDrive2;

    boolean temp = false;

    @Override
    public void robotInit() {
        xboxDrive = new XboxController(Constants.PORT_XBOX_DRIVE);
        xboxDrive2 = new XboxController(Constants.PORT_XBOX_WEAPONS);

        Dashboard.sendChooser();
       // cameraInit();
        NavX.getNavx();
        listenToA = true;
        accelerationDisable = false;
        intakeOn = false;
        forceLowGear = false;
        armControl = false;
        readyToHang = false;
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        char switchSide = ' ';
        char scaleSide = ' ';
        try {
            switchSide = gameData.charAt(0);
            scaleSide = gameData.charAt(1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("No Game Data");
        }

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
        hookSetSpeed = xboxDrive2.getX(GenericHID.Hand.kLeft);  //hook set lift thing left stick
        output = xboxDrive.getPOV(); //block output d-pad
        if(xboxDrive2.getBButtonPressed()){

        }
        //elevator.output(output);   THIS WILL BE NEEDED LATER DO NOT DELETE


        toggleAcceleration();
        togglePneumatics();
        toggleHang();

        if(readyToHang){
            DriveTrain.hang(pullSpeed);
        }
       // else {
            DriveTrain.stormDrive(combinedSpeed, 0.0, turnSpeed, accelerationDisable);

            //Use this DriveTrain.stormDrive to be able to shift gears
            //DriveTrain.stormDrive(combinedSpeed, 0.0, turnSpeed, accelerationDisable, forceLowGear);
       // }
        _hanger.setHanger(hookSetSpeed);
        _elevator.elevate(elevateSpeed, false);
        if(intakeOn) _intake.fwoo(Constants.INTAKE_SPEED);
        _elevator.elevate(elevateSpeed, false);

        if (xboxDrive.getXButtonPressed() || xboxDrive2.getXButtonPressed()) System.out.println("Doubt");
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
        String s = "Info";
        if(bug != null && bug.getName() != null) {
            s = bug.getName();
        }
        switch(s) {
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
        if(aIsPressed){
            accelerationDisable = ! accelerationDisable;
        }
    }

    private void toggleHang(){
        boolean bIsPressed = xboxDrive2.getBButton();
        if(bIsPressed){
            readyToHang = !readyToHang;
        }
    }

    private void togglePneumatics(){
        boolean yIsPressed = xboxDrive2.getYButtonPressed();
        if(yIsPressed) {
            armControl = !armControl;
        }
    }
}

