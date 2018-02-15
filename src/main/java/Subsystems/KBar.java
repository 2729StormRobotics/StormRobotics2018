package Subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import robot.Constants;

public class KBar extends Subsystem {
    private DoubleSolenoid sol;
    public KBar() {
        sol = new DoubleSolenoid(Constants.PORT_SOLENOID_KBAR_IN, Constants.PORT_SOLENOID_KBAR_OUT);
    }

    public DoubleSolenoid.Value KBarOut = DoubleSolenoid.Value.kForward;
    public DoubleSolenoid.Value KBarIn = DoubleSolenoid.Value.kForward;

    @Override
    protected void initDefaultCommand() {
    }

    public void toggleKBar(){
        if(sol.get() == KBarOut){
            setKBar(false);
        } else {
            setKBar(true);
        }
    }

    public void setKBar(boolean out) {
        if(out)
            sol.set(KBarOut);
        else
            sol.set(KBarIn);
    }

}
