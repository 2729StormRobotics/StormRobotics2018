package robot;

import AutoModes.Modes.*;
import Subsystems.*;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Mat;
import util.AutoPosition;
import util.AutoPreference;
import util.RobotState;
import util.Controller;

public class Robot extends IterativeRobot {

    public static final DriveTrain _driveTrain = new DriveTrain();
    private static final Elevator _elevator = new Elevator();
    private static final Hanger _hanger = new Hanger();
    public static final NavX navx = new NavX();
    private static final Intake _intake = new Intake();
    public static final Dashboard _dashboard = new Dashboard();
    private RobotState _robotState;
    public static final Controller _controller = new Controller();

    @Override
    public void robotInit() {
        _dashboard.sendChooser();
        //cameraInit();
        NavX.getNavx();
        _robotState = RobotState.DRIVE;
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        char switchSide = ' ';
        try {
            switchSide = gameData.charAt(0);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("No Game Data");
        }

        Command autonomousCommand = _dashboard.autoChooser.getSelected();
        AutoPosition position = _dashboard.positionChooser.getSelected();
        AutoPreference preference = _dashboard.preferenceChooser.getSelected();
        _dashboard.setBug(_dashboard.debugChooser.getSelected());

        System.out.println(autonomousCommand.getName());

        if (!autonomousCommand.getName().equalsIgnoreCase("DummyCommand")) {
            System.err.println("Auto " + _dashboard.autoChooser.getSelected() + " selected!");
            autonomousCommand.start();
            return;
        }

        switch (position.getName() + '-' + preference.getName()) {
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

        _dashboard.checkBug();
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
    public void disabledPeriodic() {
        SmartDashboard.putNumber("Elevator String Pot", Elevator.getPotHeight());
        super.disabledPeriodic();
        System.out.println(Elevator.getPotHeight());
        _dashboard.checkBug();
        NavX.dashboardStats();
        PDP.dashboardStats();
        LEDs.checkStatus();
    }

    @Override
    public void autonomousPeriodic() {
        SmartDashboard.putNumber("Elevator String Pot", Elevator.getPotHeight());
        Scheduler.getInstance().run();
        System.out.println(Elevator.getPotHeight());
        _dashboard.checkBug();
        NavX.dashboardStats();
        PDP.dashboardStats();
        LEDs.checkStatus();
    }

    @Override
    public void teleopPeriodic() {
        SmartDashboard.putNumber("Elevator String Pot", Elevator.getPotHeight());
        NavX.dashboardStats();
        PDP.dashboardStats();
        _dashboard.checkBug();
        double combinedSpeed = _controller.getForward() - _controller.getReverse();

        if(_controller.getBlockOutput())
            _elevator.outputToggle();

        if(_controller.getSmoothAccel()) {
            _driveTrain.toggleAcceleration();
        }

        if(_controller.getPTO()) {
            if(_robotState == RobotState.DRIVE) {
                _robotState = RobotState.PTO;
            } else {
                _robotState = RobotState.DRIVE;
            }
        }

        if(_robotState.getState().equalsIgnoreCase("Drive")) {
            _driveTrain.stormDrive(combinedSpeed, _controller.getTurn(), _controller.getLowGearLock());
        } else {
            _driveTrain.hang(_controller.getWinch());
        }

        _hanger.setHanger(_controller.getHanger());
        _elevator.elevate(_controller.getElevator());
        if(_controller.getArmToggle())
            _intake.toggleIntakeArm();
        if(_controller.getIntake())
            _intake.fwoo(Constants.INTAKE_SPEED);
        _controller.printDoubt();
        System.out.println(Elevator.getPotHeight());
        LEDs.checkStatus();
    }

    private static void cameraInit() {
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

}

