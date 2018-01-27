package AutoModes.Modes;

import AutoModes.Commands.ProfileFollowerWeStole;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestMode extends CommandGroup {

    public TestMode() {
        System.err.println("MidSwitch.");
        addSequential(new ProfileFollowerWeStole("ThreeFeet"));
    }

}
