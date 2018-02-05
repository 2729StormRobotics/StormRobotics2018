package AutoModes.Commands;


import Subsystems.Elevator;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import robot.Constants;
import robot.Robot;

public class Lift extends Command {

    public static double elevatorSpeed, height;
    private PIDController elevatorController;

    private PIDSource elevatorSource = new PIDSource() {
        PIDSourceType pidST;

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            pidST = pidSource;
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        public double pidGet() { // Encoder Position robot @
            /*
                get input from potentiometer
             */
            return Elevator._elevatorLeft.getSelectedSensorPosition(0);  //just an arbitrary number bc it needed to return something
        }
    };
    private PIDOutput elevatorWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            elevatorSpeed = a;
        }
    };

    public synchronized void start() {
        super.start();
        System.err.println("start Lift");
    }
    protected void end() {
        System.err.println("end Lift");
        elevatorController.disable();
        super.end();
    }

    @Override
    protected void interrupted() {
        System.err.println("interrupted Lift");
        elevatorController.disable();

        Elevator._elevatorLeft.set(ControlMode.PercentOutput, 0);
        super.interrupted();
    }
    protected void initialize() {
        super.initialize();
        elevatorController = new PIDController(Constants.ELEVATOR_P, Constants.ELEVATOR_I, Constants.ELEVATOR_D, Constants.ELEVATOR_F, elevatorSource, elevatorWrite, Constants.ELEVATOR_PERIOD); //i: 0.000003 d: 0002
        elevatorController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        elevatorController.setOutputRange(-.5, .5);
        elevatorController.setAbsoluteTolerance(Constants.ELEVATOR_TOLERANCE);
        elevatorController.setContinuous(true);
        elevatorController.setSetpoint(((Elevator._elevatorLeft.getSelectedSensorPosition(0))));
        elevatorController.enable();
        Robot._dashboard.sendElevatorEncoders();
    }

    protected void execute() {
        super.execute();
        if (!elevatorController.isEnabled()) {
            elevatorController.enable();
            System.err.println("moveElevator enabled again");
        }
        Elevator._elevatorLeft.set(ControlMode.PercentOutput, elevatorSpeed);
    }

    @Override
    protected boolean isFinished() {
        if (Math.abs(elevatorController.getError()) < Constants.TOLERANCE_TICKS) {
            elevatorController.disable();
            Elevator._elevatorLeft.set(ControlMode.PercentOutput, 0);
            return true;
        }
        return false;
    }
}
