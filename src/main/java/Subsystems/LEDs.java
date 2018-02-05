package Subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;

public class LEDs {

    public static boolean hanging, shooting, elevating, elevatingUp, armsUp, gearHigh, allianceRed, allianceBlue;

    public static SerialPort ledOut = new SerialPort(9600, SerialPort.Port.kMXP);
    private static byte ledCode[] = {(byte) 255};

    public LEDs() {
        ledOut.write(ledCode, 1);
    }

    private static void hang(){
        ledCode[0] = (byte) 170;
        ledOut.write(ledCode, 1);
    }

    private static void gear(boolean high) {
        if(high){
            ledCode[0] = (byte) 190;
        } else {
            ledCode[0] = (byte) 191;
        }
        ledOut.write(ledCode, 1);
    }

    private static void arms(boolean up){
        if(up){
            ledCode[0] = (byte) 230;
        } else {
            ledCode[0] = (byte) 231;
        }
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

    private static void elevator(boolean rising){
        if(rising){
            ledCode[0] = (byte) 246;
        } else {
            ledCode[0] = (byte) 247;
        }
        ledOut.write(ledCode, 1);
    }

    private static void shoot(){
        ledCode[0] = (byte) 252;
        ledOut.write(ledCode, 1);
    }

    private static void lightUp(String mode){lightUp(mode, false);}

    private static void lightUp(String mode, boolean state){

        switch (mode){
            case "hang":
                hang();
                break;
            case "shoot":
                shoot();
                break;
            case "elevator":
                elevator(state);
                break;
            case "arms":
                arms(state);
                break;
            case "gear":
                gear(state);
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

        allianceRed = false;
        allianceBlue = false;

        if(DriverStation.getInstance().getAlliance().toString().equals("Red")){
            allianceRed = true;
        } else if(DriverStation.getInstance().getAlliance().toString().equals("Blue")){
            allianceBlue = true;
        }

        String mode = "Idle";


    }

}
