package AutoModes.Modes;

import AutoModes.Commands.*;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class MidSwitch extends CommandGroup {

    public MidSwitch(char side) {
        System.err.println("MidSwitch.");

        if(side == 'R') {
            addSequential(new MidLeftShoot()); //its right but its simmer's fault #SimmerLogic
            //addParallel(new ProfileFollower("/home/lvuser/MotionProfiles/MidRightSwitch/_left_detailed.csv",
            //    "/home/lvuser/MotionProfiles/MidRightSwitch/_right_detailed.csv", 0.06));
            //addParallel(new OutputTimed( 3.0, 1));

            addSequential(new MoveForward(-60, 0.0006));
            addSequential(new ArmState(false));
            addSequential(new PointAndElevator(0.75, -32, false), 3);
            addSequential(new MovingIntake(55));
            addSequential(new MoveForward(-30, 0.0006));
            addSequential(new SwissArmyFoward(43, true, 2));
            addSequential(new OutputTimed(0, 1));



        }
        else if(side == 'L'){
            addSequential(new MidRightShoot()); //its right but its simmer's fault
            //addParallel(new ProfileFollower("/home/lvuser/MotionProfiles/MidLeftSwitch/_left_detailed.csv",
            //"/home/lvuser/MotionProfiles/MidLeftSwitch/_right_detailed.csv", 0.06));
            //addParallel(new OutputTimed(3.5,1));

            addSequential(new MoveForward(-60, 0.0006));
            addSequential(new ArmState(false));
            addSequential(new PointAndElevator(0.75, 32, false), 3);
            addSequential(new MovingIntake(55));
            addSequential(new MoveForward(-30, 0.0006));
            addSequential(new SwissArmyFoward(-43, true, 2));
            addSequential(new OutputTimed(0, 1));

        }
    }
}
