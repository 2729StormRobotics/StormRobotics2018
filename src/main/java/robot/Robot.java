package robot;

import AutoModes.Commands.MoveForward;
import AutoModes.Modes.LeftScale;
import AutoModes.Modes.MidSwitch;
import AutoModes.Commands.PointTurn;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
    private AHRS ahrs;



    private SendableChooser autoChooser;
    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;
    public XboxController xboxDrive;

    private static final TalonSRX _leftMain = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_LEFT_MAIN);
    private static final TalonSRX _left2 = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_LEFT_2);

    private static final TalonSRX _rightMain = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    private static final TalonSRX _right2 = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_RIGHT_2);

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


        _left2.follow(_leftMain);
        _right2.follow(_rightMain);

        //_rightMain.setInverted(true);

        xboxDrive = new XboxController(RobotMap.PORT_XBOX_DRIVE);


        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Auto.POINT_TURN, new PointTurn(ahrs, 90, _leftMain, _rightMain));
        autoChooser.addObject(Auto.MID_SWITCH, new MidSwitch(_leftMain, _rightMain, ahrs));
        autoChooser.addObject(Auto.LEFT_SCALE, new LeftScale(_leftMain, _rightMain, ahrs));
        autoChooser.addObject(Auto.POINT_TURN, new PointTurn(ahrs, 90, _leftMain, _rightMain));
        autoChooser.addObject(Auto.MOVE_FORWARD, new MoveForward(ahrs, 1516524365, _leftMain, _rightMain)); //change distance

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
            Command c = new PointTurn(ahrs, 90, _leftMain, _rightMain);
            c.start();
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

        Drives drive = new Drives(_leftMain, _rightMain);

        double combinedSpeed = forwardSpeed - reverseSpeed;
        turnSpeed = xboxDrive.getX(GenericHID.Hand.kLeft);

        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);

        drive.stormDrive(combinedSpeed, turnSpeed, true);

    }

    @Override
    public void testPeriodic() { }
}