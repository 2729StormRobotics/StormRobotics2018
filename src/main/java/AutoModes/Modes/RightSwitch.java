package AutoModes.Modes;

import AutoModes.Commands.IntakeTimed;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class RightSwitch extends CommandGroup {

    public RightSwitch() {
        addSequential(new MoveForward(145, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(-90), 1.5);
        addSequential(new MoveForward(20.6, Constants.FORWARD_LEFT_D), 1.5);
        addSequential(new IntakeTimed(0, 3));
    }
}
