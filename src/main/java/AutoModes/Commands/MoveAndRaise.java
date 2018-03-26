package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MoveAndRaise extends CommandGroup {

    public MoveAndRaise(double moveDist, double kd, double height, double delay) {
        addParallel(new MoveForward(moveDist, kd));
        addParallel(new BangBang(height, delay), 3); //10000
    }
}
