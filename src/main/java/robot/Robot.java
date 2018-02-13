package robot;

import AutoModes.Modes.*;
import Subsystems.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Mat;
import util.*;

public class Robot extends IterativeRobot {

    public static final DriveTrain _driveTrain = new DriveTrain();
    public static final Elevator _elevator = new Elevator();
    //public static final NavX navx = new NavX();
    public static final Intake _intake = new Intake();
    public static final Dashboard _dashboard = new Dashboard();
    public static final Controller _controller = new Controller();
    public static final KBar _kbar = new KBar();


    boolean idle = true;

    @Override
    public void robotInit() {
        _dashboard.sendChooser();
        cameraInit();
        //NavX.getNavx();
        _driveTrain.state = DriveState.DRIVE;
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

        SmartDashboard.putBoolean("Match Started:", true);

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
        _driveTrain.state = DriveState.DRIVE;
        _intake.state = CubeManipState.IDLE;
    }

    @Override
    public void testInit() {
    }

    @Override
    public void disabledPeriodic() {
        super.disabledPeriodic();
        _dashboard.checkBug();
        //NavX.dashboardStats();
        PDP.dashboardStats();
        LEDs.checkStatus();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        _dashboard.checkBug();
        //NavX.dashboardStats();
        PDP.dashboardStats();
        LEDs.checkStatus();
    }

    @Override
    public void teleopPeriodic() {
        //NavX.dashboardStats();
        PDP.dashboardStats();
        _dashboard.checkBug();
        double combinedSpeed = _controller.getForward() - _controller.getReverse();

        //_intake.setIntake(CubeManipState.IN);
        //_driveTrain._leftMain.set(ControlMode.PercentOutput, 0.5);
        //_intake._intakeRight.set(ControlMode.PercentOutput, 0.5);


        if(_controller.getBlockOutput()) {
            _elevator.toggleOutput();
            System.out.println("Toggling Block Output");
            System.out.println("Elevator Status" + _elevator.state);
        }

        if(_controller.getSmoothAccel()) {
            _driveTrain.toggleAcceleration();
        }
//hi - Dan Hong
        if(_controller.getPTO()) {
            if(_driveTrain.state == DriveState.DRIVE) {
                _driveTrain.state = DriveState.PTO;
            } else {
                _driveTrain.state = DriveState.DRIVE;
            }
        }

        if(_driveTrain.state.getState().equalsIgnoreCase("Drive")) {
            _driveTrain.stormDrive(combinedSpeed, _controller.getTurn(), _controller.getLowGearLock());
        } else {
            _driveTrain.hang(_controller.getWinch());
        }

        _elevator.elevate(_controller.getElevator());

        if(_controller.getArmToggle())
            _intake.toggleIntakeArm();

        System.out.println(_intake.state.getState());

        
        if(_controller.getIntake() == CubeManipState.OUT) {
            System.out.println("Intake controller OUT");
            if(_intake.state == CubeManipState.IDLE)
                _intake.setIntake(CubeManipState.OUT);
            else
                _intake.setIntake(CubeManipState.IDLE);
        } else if(_controller.getIntake() == CubeManipState.IN){
            System.out.println("Intake controller IN");
            if(_intake.state == CubeManipState.IDLE)
                _intake.setIntake(CubeManipState.IN);
            else
                _intake.setIntake(CubeManipState.IDLE);
        }

        _controller.printDoubt();
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

