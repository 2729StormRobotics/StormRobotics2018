package robot;

import AutoModes.Commands.MoveForward;
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
    public static final Elevator _elevator = new Elevator();  //Try ramp up ramp down for Elevator
    public static final NavX navx = new NavX();
    public static final Intake _intake = new Intake();
    public static final Dashboard _dashboard = new Dashboard();
    public static final Controller _controller = new Controller();
    public static final AnalogInput _proxSens = new AnalogInput(Constants.PORT_PROX_SENS);
    public static final DigitalInput _limitSwitch = new DigitalInput(Constants.PORT_LIMIT_SWITCH);

    public static double startAngle;
    public static char switchSide;
    //public static final KBar _kbar = new KBar();

    /**
     * creates a robot
     * @see IterativeRobot#robotInit()
     */
    @Override
    public void robotInit() {
        _dashboard.sendChooser();
        cameraInit();
        NavX.getNavx();
        _driveTrain.state = DriveState.DRIVE;
    }

    /**
     * runs when robot is disables
     * @see IterativeRobot#disabledInit()
     */
    @Override
    public void disabledInit() {
    }

    /**
     * when autonomous is initialized
     * @see IterativeRobot#autonomousInit()
     */
    @Override
    public void autonomousInit() {
        startAngle = NavX.getNavx().getYaw();
        _intake.setIntakeArm(true);
        _driveTrain.gearShift(true);
        _driveTrain.setPTO(false);
        SmartDashboard.putBoolean("StormDashboard/MatchStarted", true);

        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        switchSide = ' ';
        char scaleSide = ' ';
        try {
            switchSide = gameData.charAt(0);
            scaleSide = gameData.charAt(1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("No Game Data");
        }


        Command autonomousCommand = _dashboard.autoChooser.getSelected();
        AutoPosition position = _dashboard.positionChooser.getSelected();
        AutoPreference preference = _dashboard.preferenceChooser.getSelected();
        CrossPreference crossPreference = _dashboard.crossChooser.getSelected();
        _dashboard.setBug(_dashboard.debugChooser.getSelected());
        System.out.println(autonomousCommand.getName());


        if (!autonomousCommand.getName().equalsIgnoreCase("DummyCommand")) {
            System.err.println("Auto " + _dashboard.autoChooser.getSelected() + " selected!");
            autonomousCommand.start();
            return;
        }

        switch (position.getName() + '-' + preference.getName()) {
            case "Left-Scale":
                if(scaleSide == 'L')
                    autonomousCommand = new LeftScale();
                else if (crossPreference == CrossPreference.CROSS)
                    autonomousCommand = new LeftCross();
                else if(switchSide == 'L')
                    autonomousCommand = new LeftSwitch();
                else
                    autonomousCommand = new MoveForward(176.0, 0.0006);
                break;
            case "Left-Switch":
                if(switchSide == 'L')
                    autonomousCommand = new LeftSwitch();
                else if(scaleSide == 'L')
                    autonomousCommand = new LeftScale();
                else if (crossPreference == CrossPreference.CROSS)
                    autonomousCommand = new LeftCross();
                else
                    autonomousCommand = new MoveForward(175.0, 0.0006);
                break;
            case "Middle-Switch":
                autonomousCommand = new MidSwitch(switchSide);
                break;
            case "Right-Scale":
                if (scaleSide == 'R')
                    autonomousCommand = new RightScale();
                else if (switchSide == 'R')
                    autonomousCommand = new RightSwitch();
                else
                    autonomousCommand = new MoveForward(176.0, 0.0006);
                break;
            case "Right-Switch":
                if(switchSide == 'R')
                    autonomousCommand = new RightSwitch();
                else if (scaleSide == 'R')
                    autonomousCommand = new RightScale();
                else
                    autonomousCommand = new MoveForward(175.0, 0.0006);
                break;
            default: break;
        }

        _dashboard.checkBug();
        autonomousCommand.start();
        System.out.println("Running" + autonomousCommand.getName());
    }

    /**
     * when tele op is initialized
     * @see IterativeRobot#teleopInit()
     */
    @Override
    public void teleopInit() {
        _driveTrain.state = DriveState.DRIVE;
        _intake.state = CubeManipState.IDLE;

        _intake.setIntakeArm(false);
        _driveTrain.setPTO(false);
        _driveTrain.gearShift(true);
        _elevator.setStartPos();

    }

    /**
     * runs periodically when the robot is disabled
     * @see IterativeRobot#disabledPeriodic()
     */
    @Override
    public void disabledPeriodic() {
        super.disabledPeriodic();
        _elevator.setOutput(CubeManipState.IDLE, 0);
        _elevator.elevate(0);
        _dashboard.checkBug();
        NavX.dashboardStats();
        PDP.dashboardStats();
        LEDs.checkStatus();
        SmartDashboard.putString("LeftMain Control Mode", DriveTrain._leftMain.getControlMode().toString());
        SmartDashboard.putString("Left2 Control Mode", DriveTrain._left2.getControlMode().toString());
        SmartDashboard.putString("RightMain Control Mode", DriveTrain._rightMain.getControlMode().toString());
        SmartDashboard.putString("Right2 Control Mode", DriveTrain._right2.getControlMode().toString());
        SmartDashboard.putBoolean("StormDashboard/MatchStarted", false);
    }

    /**
     * runs periodically when the robot is in autonomous
     * @see IterativeRobot#autonomousPeriodic()
     */
    @Override
    public void autonomousPeriodic() {
        _elevator.setStartPos();
        Scheduler.getInstance().run();
        _dashboard.checkBug();
        NavX.dashboardStats();
        PDP.dashboardStats();
        LEDs.checkStatus();
        SmartDashboard.putString("LeftMain Control Mode", DriveTrain._leftMain.getControlMode().toString());
        SmartDashboard.putString("Left2 Control Mode", DriveTrain._left2.getControlMode().toString());
        SmartDashboard.putString("RightMain Control Mode", DriveTrain._rightMain.getControlMode().toString());
        SmartDashboard.putString("Right2 Control Mode", DriveTrain._right2.getControlMode().toString());
    }

    /**
     * runs when test is initialized
     * @see IterativeRobot#testInit()
     */
    @Override
    public void testInit() {
    }

    /**
     * runs periodically when the robot is in test
     * @see IterativeRobot#testPeriodic()
     */
    @Override
    public void testPeriodic() {

    }

    /**
     * runs periodically when the robot is in tele op
     * @see IterativeRobot#teleopPeriodic()
     */
    @Override
    public void teleopPeriodic() {
        //System.out.println(_elevator.getPotFrac());
        //System.out.println(Elevator.getTicks());
        System.out.println(_limitSwitch.get());
        NavX.dashboardStats();
        PDP.dashboardStats();
        _dashboard.checkBug();
        double combinedSpeed = _controller.getForward() - _controller.getReverse();

        SmartDashboard.putString("LeftMain Control Mode", DriveTrain._leftMain.getControlMode().toString());
        SmartDashboard.putString("Left2 Control Mode", DriveTrain._left2.getControlMode().toString());
        SmartDashboard.putString("RightMain Control Mode", DriveTrain._rightMain.getControlMode().toString());
        SmartDashboard.putString("Right2 Control Mode", DriveTrain._right2.getControlMode().toString());

        if(_controller.getLowGearLock()) {
            _driveTrain.toggleGear(); //for now this will just toggle, not hold low gear
        }

        if(_controller.getBlockOutput()) {
            if(_elevator.state == CubeManipState.IDLE)
                _elevator.setOutput(CubeManipState.OUT, 0.5);
            else
                _elevator.setOutput(CubeManipState.IDLE, 0);
            System.out.println("Toggling Block Output");
            System.out.println("Elevator Status" + _elevator.state);
        }

        if(_controller.getSmoothAccel()) {
            System.out.println("ACCELRATION TRIGGERED");
            _driveTrain.toggleAcceleration();
        }

        if(_controller.getPTO()) {
            System.out.println("PTO BEING TRIGGERED");
            _driveTrain.togglePTO();
        }

        if(_driveTrain.state.getState().equalsIgnoreCase("Drive")) {
            //_driveTrain.stormDrive(combinedSpeed, _controller.getTurn());
            _driveTrain.tankDrive(_controller.getLeftSpeed(), _controller.getRightSpeed());
        } else {
            _driveTrain.tankDrive(combinedSpeed, combinedSpeed);
        }

        if(_limitSwitch.get())
            _elevator.elevate(_controller.getElevator());

        if(_controller.getArmToggle()) {
            _intake.toggleIntakeArm();
        }
        if(_controller.getSetStart()) {
            _elevator.setStartPos();
        }

        CubeManipState controllerState = _controller.getIntake();
        if(controllerState == CubeManipState.OUT) {
            if(_intake.state == CubeManipState.IDLE)
                _intake.setIntake(CubeManipState.OUT);
            else {
                _intake.setIntake(CubeManipState.IDLE);
            }
        } else if(controllerState == CubeManipState.IN) {
            if (_intake.state == CubeManipState.IDLE)
                _intake.setIntake(CubeManipState.IN);
            else
                _intake.setIntake(CubeManipState.IDLE);
        }

        if(_proxSens.getValue() >= 690 && _intake.state == CubeManipState.IN) {
            _intake.setIntake(CubeManipState.IDLE);
            SmartDashboard.putBoolean("StormDashboard/CubeIn", true);
        } else if (_proxSens.getValue() <= 600){
            SmartDashboard.putBoolean("StormDashboard/CubeIn", false);
        }

        if(controllerState == CubeManipState.CLOCKWISE) {
            if(_intake.state == CubeManipState.IDLE)
                _intake.setIntake(CubeManipState.CLOCKWISE);
            else
                _intake.setIntake(CubeManipState.IDLE);
        } else if(controllerState == CubeManipState.COUNTERCLOCKWISE) {
            if(_intake.state == CubeManipState.IDLE)
                _intake.setIntake(CubeManipState.COUNTERCLOCKWISE);
            else
                _intake.setIntake(CubeManipState.IDLE);
        }

        _controller.printDoubt();
        LEDs.checkStatus();
        System.out.println(Elevator.getTicks());
    }

    /**
     * initializes the camera feed
     */
    private static void cameraInit() {
        new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(640, 480);
            camera.setFPS(30);

        }).start();
    }

}

