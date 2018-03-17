package AutoModes.Modes;

import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import AutoModes.Commands.ProfileFollower;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftCrossScale extends CommandGroup {

    public LeftCrossScale() {
        addSequential(new MoveForward(230, 0.015));
        addSequential(new PointTurn(90));
        addSequential(new MoveForward(204, 0.008));
        addSequential(new PointTurn(-90));
        addSequential(new MoveForward(27.96, 0.008));
    }
}
