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
        addSequential(new MoveForward(20.6, Constants.FORWARD_LEFT_D), 1.5);
        addSequential(new OutputTimed(0, 3));
    }
}
