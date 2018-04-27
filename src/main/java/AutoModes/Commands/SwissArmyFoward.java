package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class SwissArmyFoward extends CommandGroup {
    public SwissArmyFoward(double angle, boolean up, double elevatorTimeout) {
        addParallel(new BangBang(35000, 0), elevatorTimeout);
        addParallel(new PointTurn(angle, false));
        addSequential(new WaitCommand(elevatorTimeout));
        addSequential(new ArmState(up));

    }
}
