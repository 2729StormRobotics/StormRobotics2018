package robot;

import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticsPair {

    private static Solenoid solIn, solOut;

    public PneumaticsPair(int in, int out){
        solIn = new Solenoid(in);
        solOut = new Solenoid(out);
    }

    public static void setSolIn(){
        solIn.set(true);
        solOut.set(false);
    }

    public static void setSolOut(){
        solIn.set(false);
        solOut.set(true);
    }

}
