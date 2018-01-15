package robot;

import com.ctre.CANTalon;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;


import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

    private double forwardSpeed;
    private double reverseSpeed;
    private double turnSpeed;

    private static final TalonSRX _leftMain = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_LEFT_MAIN);
    private static final TalonSRX _left2 = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_LEFT_2);

    private static final TalonSRX _rightMain = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_RIGHT_MAIN);
    private static final TalonSRX _right2 = new TalonSRX(RobotMap.PORT_MOTOR_DRIVE_RIGHT_2);

    @Override
    public void robotInit() {
        _left2.follow(_leftMain);

        _right2.follow(_rightMain);


        _rightMain.setInverted(true);

        XboxController xboxDrive = new XboxController(RobotMap.PORT_XBOX_DRIVE);
        forwardSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kLeft);
        reverseSpeed = xboxDrive.getTriggerAxis(XboxController.Hand.kRight);




        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Auto.MOTION_PROF_1, new MotionProf1(_leftMain, _rightMain));
        autoChooser.addObject(Auto.MOTION_PROF_1, new MotionProf1(_leftMain, _rightMain));
        //autoChooser.addObject(Auto.MOVE_FORWARD, new MoveForward());


        SmartDashboard.putData("Autonomous Modes", autoChooser);

        Waypoint[] points = new Waypoint[] {
                new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
                new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
        };

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory trajectory = Pathfinder.generate(points, config);

        TankModifier modifier = new TankModifier(trajectory).modify(0.5);
        File myFile = new File("/home/lvuser/myfile.traj");
        Pathfinder.writeToFile(myFile, trajectory);
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

        double combinedSpeed = forwardSpeed.read() - reverseSpeed.read();
        double turn = turnSpeed.read();

        drive.stormDrive(combinedSpeed, turn, true);
    }

    @Override
    public void testPeriodic() { }
}