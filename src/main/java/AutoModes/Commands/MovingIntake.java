package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;

public class MovingIntake extends CommandGroup {
    public  MovingIntake(double forwardDist) {
        addParallel(new MoveForward(forwardDist, Constants.FORWARD_LEFT_D));
        addParallel(new IntakeCube());
    }
}
