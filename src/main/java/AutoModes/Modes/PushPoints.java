package AutoModes.Modes;

import AutoModes.Commands.MoveOnPath;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class PushPoints extends CommandGroup {
    public PushPoints(){
        addSequential(new MoveOnPath("MidRightSwitch", MoveOnPath.Direction.FORWARD));
    }
}
