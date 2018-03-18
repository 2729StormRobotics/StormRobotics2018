package AutoModes.Modes;

import AutoModes.Commands.BangBang;
import AutoModes.Commands.OutputTimed;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class RightCross extends CommandGroup {
    public RightCross() {
        addSequential(new MoveForward(225, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(-90), 2);
        addSequential(new MoveForward(185, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(90), 2);
        addSequential(new MoveForward(32, Constants.FORWARD_LEFT_D));
        addSequential(new BangBang(10000, 0), 3.5);
        addSequential(new OutputTimed(0, 2));
    }
}
