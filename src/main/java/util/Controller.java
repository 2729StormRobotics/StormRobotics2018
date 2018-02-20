package util;

import AutoModes.Commands.Lift;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import robot.Constants;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import java.sql.Timestamp;

public class Controller {

    private final XboxController mainThing;
    private final XboxController weaponsThing;
    private final Button scaleHighElevator;
    private final Button scaleMidElevator;
    private final Button switchElevator;
    private Timestamp intakePressedTime;

    public Controller() {
        mainThing = new XboxController(Constants.PORT_XBOX_DRIVE);
        weaponsThing = new XboxController(Constants.PORT_XBOX_WEAPONS);
        scaleHighElevator = new JoystickButton(weaponsThing, 4);
        scaleMidElevator = new JoystickButton(weaponsThing, 3);
        switchElevator = new JoystickButton(weaponsThing, 1);

        scaleHighElevator.whenPressed(new Lift(Constants.ELEVATOR_SCALE_HIGH_HEIGHT));  //YButton
        scaleMidElevator.whenPressed(new Lift(Constants.ELEVATOR_SCALE_MID_HEIGHT));  //XButton
        switchElevator.whenPressed(new Lift(Constants.ELEVATOR_SWITCH_HEIGHT));  //AButton

        intakePressedTime = new Timestamp(System.currentTimeMillis());
    }

    //Driver

    public double getForward() {
        return mainThing.getTriggerAxis(GenericHID.Hand.kRight);
    }

    public double getReverse() {
        return mainThing.getTriggerAxis(GenericHID.Hand.kLeft);
    }

    public double getTurn() {
        return mainThing.getX(GenericHID.Hand.kLeft);
    }

    public boolean getLowGearLock() {
        return mainThing.getBButtonPressed();
    }

    public boolean getSmoothAccel() {
        return mainThing.getAButtonPressed();
    }

    //Weapons

    public boolean getArmToggle() {
        return weaponsThing.getBButtonPressed();
    }

    public double getElevator() {
        if (Math.abs(weaponsThing.getY(GenericHID.Hand.kRight)) > 0.1) {
            System.out.println(weaponsThing.getY(GenericHID.Hand.kRight));
            return weaponsThing.getY(GenericHID.Hand.kRight);
        }
        return 0;
    }

    public CubeManipState getIntake() {
        if(weaponsThing.getBumperPressed(GenericHID.Hand.kRight)) {
            System.out.println("Controller: Intake controller IN");
            return CubeManipState.IN;
        }
        if(weaponsThing.getBumperPressed(GenericHID.Hand.kLeft)) {
            System.out.println("Controller: Intake controller OUT");
            return CubeManipState.OUT;
        }

        return CubeManipState.IDLE;
    }

    public double getWinch() {
        return weaponsThing.getTriggerAxis(GenericHID.Hand.kLeft);
    }

    public double getIntakeSpeed() {
        return weaponsThing.getTriggerAxis(GenericHID.Hand.kRight) -  weaponsThing.getTriggerAxis(GenericHID.Hand.kLeft);

    }

    public boolean getBlockOutput() {
        return weaponsThing.getXButtonPressed();
    }

    public void getElevatorSwitch() {
        weaponsThing.getXButtonPressed();
    }

    public boolean getElevatorScale() {
        return weaponsThing.getYButtonPressed();
    }

    public boolean getPTO() {
        return (mainThing.getBackButton() && mainThing.getStartButtonPressed()); //Like the CDR girl: "TRUST ME IT WORKS"
        // .get(Button)Pressed checks if it was pressed since the last check and only checks when BackButton is true
    }

    public void printDoubt() {
        if(mainThing.getXButtonPressed() || weaponsThing.getXButtonPressed()) {
            System.out.println("(X) Doubt");
        }
    }

    public static boolean isBetween(int a, int b, int c) {  //Taken from http://tech.chitgoks.com
        return b >= a ? c >= a && c <= b : c >= b && c <= a;
    }

    public double getLeftSpeed(){
        return -mainThing.getY(GenericHID.Hand.kLeft);
    }

    public double getRightSpeed(){
        return -mainThing.getY(GenericHID.Hand.kRight);
    }
}
