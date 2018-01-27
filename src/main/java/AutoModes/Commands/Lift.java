package AutoModes.Commands;


import Subsystems.Elevator;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift extends Command {

    double elevatorSpeed, height;
    PIDController elevatorController;

    PIDSource elevatorSource = new PIDSource() {
        PIDSourceType pidST;

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            pidST = pidSource;
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        public double pidGet() { // Encoder Position Robot @
            /*
                get input from potentiometer
             */
            return 0.000000234562;  //just an arbitrary number bc it needed to return something
        }
    };
    PIDOutput elevatorWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            elevatorSpeed = a;
        }
    };

    public synchronized void start() {
        super.start();
        System.err.println("start Lift");
    }

    protected void initialize() {
        super.initialize();

        elevatorController = new PIDController(0.0002, 0.0, 0.0002, 0.00, elevatorSource, elevatorWrite, 0.02); //i: 0.000003 d: 0002
        elevatorController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        elevatorController.setOutputRange(-.5, .5);
        //elevatorController.setAbsoluteTolerance(TOLERANCE_TICKS);
        elevatorController.setContinuous(true);
        elevatorController.setSetpoint(((Elevator._left.getSelectedSensorPosition(0))));
        elevatorController.enable();
    }

    protected void execute() {
        super.execute();

        if (!elevatorController.isEnabled()) {
            elevatorController.enable();
            System.err.println("moveElevator enabled again");
        }


        Elevator._left.set(ControlMode.PercentOutput, elevatorSpeed);

        SmartDashboard.putNumber("Left Encoder", Elevator._left.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder", Elevator._right.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Elevator Speed", elevatorSpeed);

        SmartDashboard.putBoolean("Left PID Enabled", elevatorController.isEnabled());
        SmartDashboard.updateValues();

    }

    @Override
    protected boolean isFinished() {
        SmartDashboard.putBoolean("Left PID Enabled", elevatorController.isEnabled());
        SmartDashboard.updateValues();

        /*
        if(Math.abs(Math.abs(current) - Math.abs(intended)) < TOLERANCE_TICKS) {
            moveController.disable();
            left.set(ControlMode.PercentOutput, 0);
            right.set(ControlMode.PercentOutput, 0);
            return true;
        }
        */


        if ((elevatorController.get() >= -0.05 && elevatorController.get() <= 0.05 && elevatorController.onTarget())) {
            elevatorController.disable();
            SmartDashboard.putBoolean("Elevator PID Enabled", elevatorController.isEnabled());
            SmartDashboard.updateValues();
            return true;
        }


        SmartDashboard.putBoolean("Elevator PID Enabled", elevatorController.isEnabled());
        SmartDashboard.updateValues();


        return false;
    }
}
