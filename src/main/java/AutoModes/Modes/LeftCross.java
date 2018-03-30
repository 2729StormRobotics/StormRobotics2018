package AutoModes.Modes;

import AutoModes.Commands.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import robot.Constants;
import robot.Robot;

public class LeftCross extends CommandGroup {

    public LeftCross() {
        Robot._driveTrain.gearShift(false);
        addSequential(new MoveForward(221, 0.0006)); //204
        addSequential(new WaitCommand(0.5));
        addSequential(new PointTurn(87, true), 2);
        addSequential(new MoveForward(220, 0.0006));
        addSequential(new PointTurn(-95, false), 2);
        addSequential(new MoveAndRaise(47, 0.0001, 35000, 0));
        addSequential(new OutputTimed(0, 2));
        addSequential(new ArmState(false));
        addSequential(new BangBang(-35000, 0), 3);
        //addSequential(new PointTurn(, false)); for later
    }

}
//drive 5 sec
//elevator 2.5 sec
