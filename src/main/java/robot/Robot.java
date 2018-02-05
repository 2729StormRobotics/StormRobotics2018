package robot;

import AutoModes.Modes.*;
import Subsystems.*;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.opencv.core.Mat;
import util.AutoPosition;
import util.AutoPreference;
import util.DebugLevel;
import util.RobotState;
import util.Controller;

public class Robot extends IterativeRobot {

    public static DriveTrain _driveTrain = new DriveTrain();
    public static Elevator _elevator = new Elevator();
    public static Hanger _hanger = new Hanger();
    public static NavX navx = new NavX();
    public static Intake _intake = new Intake();
    public static Dashboard _dashboard = new Dashboard();
    public static RobotState _robotState;
    private Controller _controller = new Controller();

    public static boolean acceleration;

    boolean intakePneumatics = false;

    @Override
    public void robotInit() {
        _dashboard.sendChooser();
        cameraInit();
        NavX.getNavx();
        acceleration = false;
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
        char scaleSide = ' ';
        try {
            switchSide = gameData.charAt(0);
            scaleSide = gameData.charAt(1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("No Game Data");
        }

        Command autonomousCommand = (Command) _dashboard.autoChooser.getSelected();
        AutoPosition position = (AutoPosition) _dashboard.positionChooser.getSelected();
        AutoPreference preference = (AutoPreference) _dashboard.preferenceChooser.getSelected();
        _dashboard.bug = (DebugLevel) _dashboard.debugChooser.getSelected();

        System.out.println(autonomousCommand.getName());

        if (!autonomousCommand.getName().equalsIgnoreCase("DummyCommand")) {
            System.err.println("Auto " + _dashboard.autoChooser.getSelected() + " selected!");
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
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        _dashboard.checkBug();
        NavX.dashboardStats();
    }

    @Override
    public void teleopPeriodic() {
        NavX.dashboardStats();
        _dashboard.checkBug();
        double combinedSpeed = _controller.getForward() - _controller.getReverse();

        if(_controller.getBlockOutput())
            _elevator.outputToggle();

        if(_controller.getSmoothAccel()) {
            _driveTrain.toggleAcceleration();
        }

        if(_controller.getPTO()) {
            if(_robotState.equals(RobotState.DRIVE)) {
                _robotState = RobotState.PTO;
            }
        }

        if(_robotState.getState().equalsIgnoreCase("Drive")) {
            _driveTrain.stormDrive(combinedSpeed, _controller.getTurn(), _controller.getLowGearLock());
        }
        else {
            _driveTrain.hang(_controller.getWinch());
        }

        _hanger.setHanger(_controller.getHanger());
        _elevator.elevate(_controller.getElevator());
        if(_controller.getArmToggle())
            _intake.toggleIntakeArm();
        if(_controller.getIntake())
            _intake.fwoo(Constants.INTAKE_SPEED);
        _controller.printDoubt();
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

}

