package AutoModes.Modes;

import AutoModes.Commands.PointTurn;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestMode extends CommandGroup {

    public TestMode() {
        addSequential(new PointTurn(90, false));
        addSequential(new PointTurn(90, false));
        addSequential(new PointTurn(45, false));
        //addSequential(new CommandsWut(), 5);
    }

}
