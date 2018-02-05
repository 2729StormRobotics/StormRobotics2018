package Subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;

public class LEDs {

    public static boolean hanging, shooting, elevatingUp, armsUp, gearHigh, allianceRed, allianceBlue;

    public static SerialPort ledOut = new SerialPort(9600, SerialPort.Port.kMXP);
    private static byte ledCode[] = {(byte) 255};

    public LEDs() {
        ledOut.write(ledCode, 1);
    }

    private static void hang(){
        ledCode[0] = (byte) 170;
        ledOut.write(ledCode, 1);
    }

    private static void gear() {
        ledCode[0] = (byte) 190;
        ledOut.write(ledCode, 1);
    }

    private static void arms(){
        ledCode[0] = (byte) 230;
        ledOut.write(ledCode, 1);
    }

    private static void alliance(boolean red){
        if(red){
            ledCode[0] = (byte) 236;
        } else {
            ledCode[0] = (byte) 237;
        }
        ledOut.write(ledCode, 1);
    }

    private static void idle(){
        ledCode[0] = (byte) 0;
        ledOut.write(ledCode, 1);
    }

    private static void elevator(){
        ledCode[0] = (byte) 246;
        ledOut.write(ledCode, 1);
    }

    private static void shoot(){
        ledCode[0] = (byte) 252;
        ledOut.write(ledCode, 1);
    }

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

    public static void checkStatus() {

        String mode = "Idle";
        boolean state = false;

        allianceRed = false;
        allianceBlue = false;

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
