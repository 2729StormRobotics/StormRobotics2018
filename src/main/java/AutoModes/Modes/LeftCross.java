package AutoModes.Modes;

import AutoModes.Commands.BangBang;
import AutoModes.Commands.OutputTimed;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class LeftCross extends CommandGroup {

    public LeftCross() {
        addSequential(new MoveForward(204, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(90, false), 2);
        addSequential(new MoveForward(180, Constants.FORWARD_LEFT_D));
        addSequential(new PointTurn(-90, false), 2);
        addSequential(new MoveForward(32, Constants.FORWARD_LEFT_D));
        addSequential(new BangBang(10000, 0), 3.5); //10000
        addSequential(new OutputTimed(0, 2));
    }

}
//drive 5 sec
//elevator 2.5 sec
