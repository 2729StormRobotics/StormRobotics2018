package robot;


import AutoModes.LeftSwitch;
import AutoModes.MidSwitch;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
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
    }

    @Override
    public void robotInit() {
        _left2.follow(_leftMain);

        _right2.follow(_rightMain);


        _rightMain.setInverted(true);

        XboxController xboxDrive = new XboxController(RobotMap.PORT_XBOX_DRIVE);
        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);

        //drive = new TankDrive(_leftMain, _rightMain);


        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Auto.MID_SWITCH, new MidSwitch(_leftMain, _rightMain, ahrs));
        autoChooser.addObject(Auto.MID_SWITCH, new MidSwitch(_leftMain, _rightMain, ahrs));
        autoChooser.addObject(Auto.LEFT_SWITCH, new LeftSwitch(_leftMain, _rightMain, ahrs));
        //autoChooser.addObject(Auto.MOVE_FORWARD, new MoveForward());

        SmartDashboard.putData("Autonomous Modes", autoChooser);

    }

    @Override
    public void disabledInit() { }

    @Override
    public void autonomousInit() {
        Command autonomousCommand = (Command) autoChooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }
    }

    @Override
    public void teleopInit() { }

    @Override
    public void testInit() { }


    @Override
    public void disabledPeriodic() { }
    
    @Override
    public void autonomousPeriodic() { }

    @Override
    public void teleopPeriodic() {
        Drives drive = new Drives(_leftMain, _rightMain);

        //Strongback.logger().warn("Left Speed: " + leftSpeed.read() + "          Right Speed: " + rightSpeed.read());

        double combinedSpeed = forwardSpeed - reverseSpeed;
        double turn = turnSpeed;

        drive.stormDrive(combinedSpeed, turn, true);
    }

    @Override
    public void testPeriodic() { }
}