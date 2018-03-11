package AutoModes.Modes;

import AutoModes.Commands.BangBang;
import AutoModes.Commands.IntakeTimed;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class LeftCross extends CommandGroup {

    public LeftCross() {
        addSequential(new MoveForward(225, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(90), 2);
        addSequential(new MoveForward(180, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(-90), 2);
        addSequential(new MoveForward(32, Constants.FORWARD_LEFT_D));
        addSequential(new BangBang(10000), 3.5);
        addSequential(new IntakeTimed(0, 2));
    }

}
