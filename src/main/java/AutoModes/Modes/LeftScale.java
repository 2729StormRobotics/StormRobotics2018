package AutoModes.Modes;

import AutoModes.Commands.*;
import Subsystems.NavX;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.Robot;

public class LeftScale extends CommandGroup {

    public LeftScale() {
        System.err.println("LeftScale.");
        //addSequential(new MoveForward(176, Constants.FORWARD_LEFT_D));
        addSequential(new ProfileFollower("/home/lvuser/MotionProfiles/LeftScaleAngled/_left_detailed.csv",
                "/home/lvuser/MotionProfiles/LeftScaleAngled/_right_detailed.csv", 0.05));
        //addSequential(new PointTurn(45), 2);
        addSequential(new BangBang(8000, 0), 3.5);
        addSequential(new IntakeTimed(0, 2));

        //addSequential(new PointTurn(Constants.twoCubeAngle - Robot.startAngle - NavX.getNavx().getYaw())); To Turn to -170 relative to auto start

    }

}
