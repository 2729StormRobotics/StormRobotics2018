package AutoModes.Modes;

import AutoModes.Commands.OutputTimed;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class LeftSwitch extends CommandGroup {
    public LeftSwitch() {
        addSequential(new MoveForward(145, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(90, false), 1.5);
        addSequential(new MoveForward(25.6, Constants.FORWARD_LEFT_D), 2.0);
        addSequential(new OutputTimed(0, 1));
    }
}
