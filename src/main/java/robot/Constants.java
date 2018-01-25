package robot;

public class Constants {

    public static final int PORT_MOTOR_DRIVE_LEFT_MAIN 			= 1; //Front Left
    public static final int PORT_MOTOR_DRIVE_LEFT_2 			= 2; //Back left
    public static final int PORT_MOTOR_DRIVE_RIGHT_MAIN 		= 11;//Front right
    public static final int PORT_MOTOR_DRIVE_RIGHT_2 			= 12;//Back right

    public static final int PORT_XBOX_DRIVE                     = 0;
    public static final int PORT_XBOX_WEAPONS                   = 1;

    public static final int TICKS_PER_REV                       = 1024;

    public static final double TURNCONTROLLER_P                 = 0.006;
    public static final double TURNCONTROLLER_I                 = 0.006;
    public static final double TURNCONTROLLER_D                 = 0.0002;
    public static final double TURNCONTROLLER_F                 = 0.005;
    public static final double TURNCONTROLLER_PERIOD            = 0.02;

    public static final double POINT_TURN_TOLERANCE             = 2.0;

}
