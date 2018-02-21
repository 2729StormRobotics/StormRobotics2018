package robot;

public class Constants {

    public static final int PORT_MOTOR_DRIVE_LEFT_MAIN 			= 14; //Front Left
    public static final int PORT_MOTOR_DRIVE_LEFT_2 			= 1; //Back left
    public static final int PORT_MOTOR_DRIVE_RIGHT_MAIN 		= 0;//Front right
    public static final int PORT_MOTOR_DRIVE_RIGHT_2 			= 15;//Back right

    public static final int PORT_MOTOR_DRIVE_ELEVATOR_MAIN 		= 3;//Elevator 1
    public static final int PORT_MOTOR_DRIVE_ELEVATOR_2 		= 12;//Elevator 2
    public static final int PORT_STRING_POT 		            = 3;//Elevator 2

    public static final int PORT_MOTOR_INTAKE_LEFT              = 6; //Intake Left
    public static final int PORT_MOTOR_INTAKE_RIGHT             = 7; //Intake Right

    public static final int PORT_MOTOR_OUTPUT_LEFT              = 5; //Output Left
    public static final int PORT_MOTOR_OUTPUT_RIGHT             = 10;//Output Right


    public static final int PORT_SOLENOID_INTAKE_IN             = 1; //Left Solenoid
    public static final int PORT_SOLENOID_INTAKE_OUT            = 2; //Left Solenoid
    public static final int PORT_SOLENOID_GEARSHIFT_IN          = 0;
    public static final int PORT_SOLENOID_GEARSHIFT_OUT         = 3;
    public static final int PORT_SOLENOID_PTO_IN                = 7;
    public static final int PORT_SOLENOID_PTO_OUT               = 5;
    public static final int PORT_SOLENOID_KBAR_IN               = 6;
    public static final int PORT_SOLENOID_KBAR_OUT              = 4;


    public static final int PORT_XBOX_DRIVE                     = 0;
    public static final int PORT_XBOX_WEAPONS                   = 1;

    public static final double MOTOR_TOLERANCE_MAX              = 0.25;  //Drive Motor Tolerances
    public static final double MOTOR_TOLERANCE_DEFAULT          = 0.04;
    public static final double MOTOR_TOLERANCE_MIN              = 0.01;

    public static final double MIN_TURN_SPEED                   = 0.05;
    public static final double TICKS_PER_REV                    = 1024.0 * 4;

    public static final double WHEEL_SIZE                       = 6.0 * 3.14;
    public static final double TOLERANCE_TICKS                  = (TICKS_PER_REV) / 5.0;
    public static final double TOLERANCE_DEGREES                = 0.5;

    public static final double TURNCONTROLLER_P                 = 0.015;
    public static final double TURNCONTROLLER_I                 = 0.0000;
    public static final double TURNCONTROLLER_D                 = 0.05;
    public static final double TURNCONTROLLER_F                 = 0.0;
    public static final double TURNCONTROLLER_PERIOD            = 0.02;
    public static final double POINT_TURN_TOLERANCE             = 2.0;

    public static final double FORWARD_LEFT_P                   = 0.000035;
    public static final double FORWARD_LEFT_I                   = 0.0; //0.000004
    public static final double FORWARD_LEFT_D                   = 0.00003; //0.008
    public static final double FORWARD_LEFT_F                   = 0.0;
    public static final double FORWARD_LEFT_PERIOD              = 0.02; //0.02

    public static final double FORWARD_RIGHT_P                  = 0.000035;
    public static final double FORWARD_RIGHT_I                  = 0.0;
    public static final double FORWARD_RIGHT_D                  = 0.00003;
    public static final double FORWARD_RIGHT_F                  = 0.0;
    public static final double FORWARD_RIGHT_PERIOD             = 0.02;

    public static final double FORWARD_ANGLE_P                  = 0.8;
    public static final double FORWARD_ANGLE_I                  = 0.0;
    public static final double FORWARD_ANGLE_D                  = 0.04;
    public static final double FORWARD_ANGLE_F                  = 0.0;
    public static final double FORWARD_ANGLE_PERIOD             = 0.02;

    public static final double INTAKE_SPEED                     = 0.75;

    public static final double OUTPUT_SPEED                     = 1.0;
    public static final double CART_IN_SPEED                    = 0.5;

    //do some calculations to determine optimal levels of motor output for shifting up and down
    public static final double SHIFT_UP                         = 0.8;
    public static final double SHIFT_DOWN                       = 0.4;

    public static final double ELEVATOR_P                       = 0.0002;
    public static final double ELEVATOR_I                       = 0.0000;
    public static final double ELEVATOR_D                       = 0.00005;
    public static final double ELEVATOR_F                       = 0.0;
    public static final double ELEVATOR_PERIOD                  = 0.02;
    public static final double ELEVATOR_TOLERANCE               = 2.0;
    public static final double ELEVATOR_MAX                     = 0.0; //max height in inches
    public static final double ELEVATOR_ENCODER_RANGE           = 0.0; //test 5 times going to the max height of the scale and subtract from the zeroPos to get range
    public static final double ELEVATOR_INCH_PER_REV            = 2.0 * Math.PI; //Change 2 later for actual diameter
    public static final double ELEVATOR_TICKS_PER_INCH          = TICKS_PER_REV / ELEVATOR_INCH_PER_REV;
    public static final double ELEVATOR_MAX_TICKS               = ELEVATOR_MAX * ELEVATOR_TICKS_PER_INCH;

    public static final double ELEVATOR_STAGE_ONE_MAX           = 31.0;
    public static final double ELEVATOR_STAGE_TWO_MAX           = 38.0; //not right, gotta measure
    public static final double ELEVATOR_STAGE_THREE_MAX         = 38.0;
    public static final double ELEVATOR_STAGE_FOUR_MAX          = 39.0;
    public static final double ELEVATOR_SWITCH_HEIGHT           = 40.0; //just a guess
    public static final double ELEVATOR_SCALE_MID_HEIGHT        = 60.0;
    public static final double ELEVATOR_SCALE_HIGH_HEIGHT       = 72.0;

    public static final double STRPOT_MAX                       = 56.0; //push
    public static final double STRPOT_START_FRACTION            = 0.1;
    public static final double STRPOT_SWITCH_FRACTION           = 0.6426; //36.0 in inches (Should be 0.6426)
    public static final double ELEVATOR_SLOW_DOWN_FRACTION      = 0.25;

//    public static final double SHIFT_UP_MULT                  = 0.406;
//    public static final double SHIFT_DOWN_MULT                = 2.06;

    static final String MID_SWITCH                              = "Mid Switch";
    static final String LEFT_SCALE                              = "Left Side Scale";
    static final String LEFT_SWITCH                             = "Left Side Switch";
    static final String RIGHT_SCALE                             = "Right Scale";
    static final String RIGHT_SWITCH                            = "Right Switch";
    static final String POINT_TURN                              = "Point Turn";
    static final String MOVE_FORWARD                            = "Move Forward";
    static final String TEST_MODE                               = "Test Mode";
    static final String FOLLOW_PREF                             = "Follow";
    static final String INTAKE_TIMED                            = "Intake Timed";

    public static final int CONTROLLER_INTAKE_BUFFER            = 250; //Time in ms where the intake controller stops listening.  (prevents double clicks)
}
