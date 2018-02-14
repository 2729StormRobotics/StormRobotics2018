package util;

import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticsPair {

    public static Solenoid solIn, solOut;

    public PneumaticsPair(int in, int out){
        solIn = new Solenoid(in);
        solOut = new Solenoid(out);
    }

    public void set(boolean out) {
        solIn.set(out);
        solOut.set(!out);
    }

    public boolean get() {
        return solOut.get();
    }

}
