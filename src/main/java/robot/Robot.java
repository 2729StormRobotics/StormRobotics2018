package robot;


import AutoModes.Modes.LeftSwitch;
import AutoModes.Modes.LeftScale;
import AutoModes.Modes.MidSwitch;
import AutoModes.Commands.PointTurn;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
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

    private static final TalonSRX _leftMain = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_LEFT_MAIN);
    private static final TalonSRX _left2 = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_LEFT_2);

    private static final TalonSRX _rightMain = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    private static final TalonSRX _right2 = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_RIGHT_2);

    public static final class Auto{
        public static final String MID_SWITCH = "Mid Switch";
        public static final String LEFT_SWITCH = "Left Side Switch";
        public static final String POINT_TURN = "Point Turn";
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

        XboxController xboxDrive = new XboxController(RobotMap.PORT_XBOX_DRIVE);
        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);

        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Auto.POINT_TURN, new PointTurn(ahrs, 90, _leftMain, _rightMain));
        autoChooser.addObject(Auto.MID_SWITCH, new MidSwitch(_leftMain, _rightMain, ahrs));
        autoChooser.addObject(Auto.LEFT_SWITCH, new LeftSwitch(_leftMain, _rightMain, ahrs));
        autoChooser.addObject(Auto.POINT_TURN, new PointTurn(ahrs, 90, _leftMain, _rightMain));

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
        double turn = turnSpeed;

        //drive.stormDrive(combinedSpeed, turn, true);

    }

    @Override
    public void testPeriodic() { }
}