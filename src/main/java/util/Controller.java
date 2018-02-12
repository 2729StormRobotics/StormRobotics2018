package util;

import robot.Constants;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class Controller {

    private final XboxController mainThing;
    private final XboxController weaponsThing;

    public Controller() {
        mainThing = new XboxController(Constants.PORT_XBOX_DRIVE);
        weaponsThing = new XboxController(Constants.PORT_XBOX_WEAPONS);
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
        return mainThing.getBButton();
    }

    public boolean getSmoothAccel() {
        return mainThing.getAButtonPressed();
    }

    //Weapons

    public boolean getArmToggle() {
        return weaponsThing.getBButtonPressed();
    }

    public double getElevator() {
        return weaponsThing.getY(GenericHID.Hand.kRight);
    }

    public double getWinch() {
        return weaponsThing.getTriggerAxis(GenericHID.Hand.kLeft);
    }

    public int getIntake() {
        return weaponsThing.getPOV();
    }

    public boolean getBlockOutput() {
        return weaponsThing.getBumperPressed(GenericHID.Hand.kRight);
    }

    public boolean getElevatorSwitch() {
        return weaponsThing.getXButtonPressed();
    }

    public boolean getElevatorScale() {
        return weaponsThing.getYButtonPressed();
    }

    public boolean getPTO() {
        return (weaponsThing.getBackButton() && weaponsThing.getStartButtonPressed()); //Like the CDR girl: "TRUST ME IT WORKS"
        // .get(Button)Pressed checks if it was pressed since the last check and only checks when BackButton is true
    }

    public void printDoubt() {
        if(mainThing.getXButtonPressed() || weaponsThing.getXButtonPressed()) {
            System.out.println("(X) Doubt");
        }
    }

}
