package AutoModes.Commands;


import Subsystems.Elevator;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import robot.Constants;
import robot.Robot;

public class Lift extends Command {

    double setPoint;
    public static double  elevatorSpeed;
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
            return Elevator._elevator.getSelectedSensorPosition(0);  //just an arbitrary number bc it needed to return something
        }
    };

    private PIDOutput elevatorWrite = new PIDOutput() {
        public void pidWrite(double a) {
            elevatorSpeed = a;
        }
    };

    public Lift(double inches) {
        setPoint = Elevator.checkHeight(inches * Constants.ELEVATOR_TICKS_PER_INCH);
    }

    public synchronized void start() {
        super.start();
        System.err.println("start Lift");
    }
    protected void end() {
        System.err.println("end Lift");
        elevatorController.disable();
        super.end();
    }

    /**
     * In the case the Command is interrupted turn off Elevator
     * @see Command#interrupted()
     */
    @Override
    protected void interrupted() {
        System.err.println("interrupted Lift");
        elevatorController.disable();

        Elevator._elevator.set(ControlMode.PercentOutput, 0);
        super.interrupted();
    }

    /**
     * Sets up PID controller for Elevator
     * @see Command#initialize()
     */
    protected void initialize() {
        super.initialize();
        elevatorController = new PIDController(Constants.ELEVATOR_P, Constants.ELEVATOR_I, Constants.ELEVATOR_D, Constants.ELEVATOR_F, elevatorSource, elevatorWrite, Constants.ELEVATOR_PERIOD); //i: 0.000003 d: 0002
        elevatorController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        elevatorController.setOutputRange(-.5, .5);
        elevatorController.setAbsoluteTolerance(Constants.ELEVATOR_TOLERANCE);
        elevatorController.setContinuous(true);
        elevatorController.setSetpoint(setPoint);
        elevatorController.enable();
    }

    /**
     * Calculates desired motor output speed using PID controller.
     * @see Command#execute()
     */
    protected void execute() {
        super.execute();
        if (!elevatorController.isEnabled()) {
            elevatorController.enable();
            System.err.println("moveElevator enabled again");
        }
        Elevator._elevator.set(ControlMode.PercentOutput, elevatorSpeed);
    }

    /**
     * Checks if Command is done.  If it's done set elevator motor to 0
     * @return true means finished.  False means to call execute again
     * @see Command#isFinished()
     */
    @Override
    protected boolean isFinished() {
        if (Math.abs(elevatorController.getError()) < Constants.TOLERANCE_TICKS) {
            elevatorController.disable();
            Elevator._elevator.set(ControlMode.PercentOutput, 0);
            return true;
        }
        return false;
    }
}
