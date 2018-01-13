package robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    @Override
    public void robotInit() {
        _left2.changeControlMode(CANTalon.TalonControlMode.Follower);
        _left2.set(_leftMain.getDeviceID());

        _right2.changeControlMode(CANTalon.TalonControlMode.Follower);
        _right2.set(_rightMain.getDeviceID());


        _rightMain.setInverted(true);

        Gamepad xboxDrive = Hardware.HumanInterfaceDevices.xbox360(RobotMap.PORT_XBOX_DRIVE);

        forwardSpeed = xboxDrive.getRightTrigger();
        reverseSpeed = xboxDrive.getLeftTrigger();
        turnSpeed = xboxDrive.getRightX();

        //drive = new TankDrive(_leftMain, _rightMain);


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