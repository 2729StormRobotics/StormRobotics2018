package AutoModes.Modes;

import AutoModes.Commands.IntakeTimed;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightSwitch extends CommandGroup {

    public RightSwitch() {
        addSequential(new MoveForward(150));
        addSequential(new PointTurn(-90), 3);
        addSequential(new MoveForward(20.6), 3);
        addSequential(new IntakeTimed(0, 3));
    }
}
