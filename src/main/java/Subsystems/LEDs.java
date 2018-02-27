package Subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;

public class LEDs {

    public static boolean hanging;
    public static boolean shooting;
    public static boolean elevatingUp;
    public static boolean armsUp;
    public static boolean gearHigh;

    //private static SerialPort ledOut = new SerialPort(9600, SerialPort.Port.kMXP);
    private static byte ledCode[] = {(byte) 255};

    /**
     * initializes led connection
     */
    public LEDs() {
        //ledOut.write(ledCode, 1);
    }

    /**
     * sends the led code for hanging
     */
    private static void hang(){
        ledCode[0] = (byte) 170;
        //ledOut.write(ledCode, 1);
    }

    /**
     * sends the led code for high gear
     */
    private static void gear() {
        ledCode[0] = (byte) 190;
        //ledOut.write(ledCode, 1);
    }

    /**
     * sends the led code for arms up
     */
    private static void arms(){
        ledCode[0] = (byte) 230;
        //ledOut.write(ledCode, 1);
    }

    /**
     * sends the led code for which alliance we're on
     * @param red red or blue alliance
     */
    private static void alliance(boolean red){
        if(red){
            ledCode[0] = (byte) 236;
        } else {
            ledCode[0] = (byte) 237;
        }
        //ledOut.write(ledCode, 1);
    }

    /**
     * sends the led code for idle
     */
    private static void idle(){
        ledCode[0] = (byte) 0;
        //ledOut.write(ledCode, 1);
    }

    /**
     * sends the led code for elevator down
     */
    private static void elevator(){
        ledCode[0] = (byte) 246;
        //ledOut.write(ledCode, 1);
    }

    /**
     * sends the led code for shooting
     */
    private static void shoot(){
        ledCode[0] = (byte) 252;
        //ledOut.write(ledCode, 1);
    }

    /**
     * calls the send functions
     * @param mode which function to call
     * @param state if the function requires a state what is it
     */
    private static void lightUp(String mode, boolean state){

        switch (mode){
            case "hang":
                hang();
                break;
            case "shoot":
                shoot();
                break;
            case "elevator":
                elevator();
                break;
            case "arms":
                arms();
                break;
            case "gear":
                gear();
                break;
            case "alliance":
                alliance(state);
                break;
            case "idle":
                idle();
                break;
        }

    }

    /**
     * checks the status of each subsystem in the hierarchy order reversed
     */
    public static void checkStatus() {

        String mode = "Idle";
        boolean state = false;

        boolean allianceRed = false;
        boolean allianceBlue = false;

        if(DriverStation.getInstance().getAlliance().toString().equals("Red")){
            allianceRed = true;
        } else if(DriverStation.getInstance().getAlliance().toString().equals("Blue")){
            allianceBlue = true;
        }

        if (allianceRed || allianceBlue){
            mode = "alliance";
            if (allianceRed){
               state = true;
            }
        }

        if (gearHigh){
            mode = "gear";
        }

        if (armsUp){
            mode = "arms";
        }

        if(elevatingUp){
            mode = "elevator";
        }

        if(shooting){
            mode = "shoot";
        }

        if(hanging){
            mode = "hang";
        }

        lightUp(mode, state);

    }
}
