package robot;

public class Constants {

    public static final int PORT_MOTOR_DRIVE_LEFT_MAIN 			= 1; //Front Left
    public static final int PORT_MOTOR_DRIVE_LEFT_2 			= 2; //Back left
    public static final int PORT_MOTOR_DRIVE_RIGHT_MAIN 		= 11;//Front right
    public static final int PORT_MOTOR_DRIVE_RIGHT_2 			= 12;//Back right

    public static final int PORT_MOTOR_DRIVE_ELEVATOR_MAIN 		= 5;//Elevator 1
    public static final int PORT_MOTOR_DRIVE_ELEVATOR_2 		= 10;//Elevator 2

    public static final int PORT_MOTOR_DRIVE_HANG_MAIN 		    = 7;//Hanger

    public static final int PORT_MOTOR_INTAKE_LEFT              = 4; //Intake Left
    public static final int PORT_MOTOR_INTAKE_RIGHT             = 6; //Intake Right

    public static final int PORT_MOTOR_OUTPUT_LEFT              = 13; //Output Left
    public static final int PORT_MOTOR_OUTPUT_RIGHT             = 14;//Output Right

    public static final int PORT_SOLENOID_INTAKE            = 0;//Left Solenoid

    public static final int PORT_SOLENOID_GEARSHIFT             = 1;


    public static final int PORT_XBOX_DRIVE                     = 0;
    public static final int PORT_XBOX_WEAPONS                   = 1;

    public static final int TICKS_PER_REV                       = 1024;

    public static final double WHEEL_SIZE = 4.0 * 3.14;
    public static final double TOLERANCE_TICKS = (TICKS_PER_REV) / 5;
    public static final double TOLERANCE_DEGREES = 0.5;

    public static final double TURNCONTROLLER_P                 = 0.006;
    public static final double TURNCONTROLLER_I                 = 0.0000;
    public static final double TURNCONTROLLER_D                 = 0.00005;
    public static final double TURNCONTROLLER_F                 = 0.0;
    public static final double TURNCONTROLLER_PERIOD            = 0.02;
    public static final double POINT_TURN_TOLERANCE             = 2.0;

    public static final double FORWARD_LEFT_P                   = 0.0002;
    public static final double FORWARD_LEFT_I                   = 0.000004;
    public static final double FORWARD_LEFT_D                   = 0.008;
    public static final double FORWARD_LEFT_F                   = 0.0;
    public static final double FORWARD_LEFT_PERIOD              = 0.02;

    public static final double FORWARD_RIGHT_P                  = 0.0002;
    public static final double FORWARD_RIGHT_I                  = 0.000004;
    public static final double FORWARD_RIGHT_D                  = 0.008;
    public static final double FORWARD_RIGHT_F                  = 0.0;
    public static final double FORWARD_RIGHT_PERIOD             = 0.02;

    public static final double FORWARD_ANGLE_P                  = 0.05;
    public static final double FORWARD_ANGLE_I                  = 0.0;
    public static final double FORWARD_ANGLE_D                  = 0.04;
    public static final double FORWARD_ANGLE_F                  = 0.0;
    public static final double FORWARD_ANGLE_PERIOD             = 0.02;

    public static final double INTAKE_SPEED                     = 0.4;


    //do some calculations to determine optimal levels of motor output for shifting up and down
    public static final double SHIFT_UP                         = 0.8;
    public static final double SHIFT_DOWN                       = 0.4;

    public static final double ELEVATOR_P                 = 0.0002;
    public static final double ELEVATOR_I                 = 0.0000;
    public static final double ELEVATOR_D                 = 0.00005;
    public static final double ELEVATOR_F                 = 0.0;
    public static final double ELEVATOR_PERIOD            = 0.02;
    public static final double ELEVATOR_TOLERANCE         = 2.0;

    public static final double SHIFT_UP_MULT              =0.406;
    public static final double SHIFT_DOWN_MULT            =2.06;

    public static final String MID_SWITCH = "Mid Switch";
    public static final String LEFT_SCALE = "Left Side Scale";
    public static final String LEFT_SWITCH = "Left Side Switch";
    public static final String RIGHT_SCALE = "Right Scale";
    public static final String RIGHT_SWITCH = "Right Switch";
    public static final String POINT_TURN = "Point Turn";
    public static final String MOVE_FORWARD = "Move Forward";
    public static final String TEST_MODE = "Test Mode";
    public static final String FOLLOW_PREF = "Follow";


}
