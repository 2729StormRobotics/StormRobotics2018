package Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;
import util.PneumaticsPair;

public class KBar extends Subsystem {
    private PneumaticsPair sol;
    public KBar() {
        sol = new PneumaticsPair(Constants.PORT_SOLENOID_KBAR_IN, Constants.PORT_SOLENOID_KBAR_OUT);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void toggleKbar(){
        sol.set(!sol.get());

        if(!sol.get()){
            LEDs.armsUp = true;
        }
    }

    public void setIntakeArm(boolean _intakeArmOut) {
        sol.set(_intakeArmOut);
    }

}
