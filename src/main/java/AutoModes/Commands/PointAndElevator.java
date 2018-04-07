package AutoModes.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PointAndElevator extends CommandGroup {

    public PointAndElevator(double _speed, double _angle, boolean _abs) {
        addParallel(new PointTurn(_angle, _abs));
        addParallel(new BangBang(-35000, 0), 4);
    }

}
