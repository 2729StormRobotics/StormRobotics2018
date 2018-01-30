package AutoModes.Modes;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightSwitch extends CommandGroup {

    public RightSwitch() {
        addSequential(new MoveForward(154));
        addSequential(new PointTurn(-90), 3);
        addSequential(new MoveForward(20.6));
    }
}
