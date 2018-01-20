package AutoModes.Commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Constants;

public class MoveForward extends Command {

    AHRS ahrs;
    TalonSRX left, right;
    double moveSpeed, distance;
    PIDController moveController;

    PIDSource angleSource = new PIDSource() {
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
            SmartDashboard.putNumber("Encoder:", -right.getSelectedSensorPosition(0));
            return -right.getSelectedSensorPosition(0);

        }
    };

    PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            moveSpeed = a;  //change to -a later when .setInverted works
            left.set(ControlMode.PercentOutput, moveSpeed);
            right.set(ControlMode.PercentOutput, -moveSpeed);
            SmartDashboard.putNumber("Encoder:", -right.getSelectedSensorPosition(0));
        }
    };

    static final double toleranceInches = 5.0 * Constants.TICKS_PER_REV;

    public MoveForward(AHRS _ahrs, double _dist, TalonSRX _left, TalonSRX _right) {
        ahrs = _ahrs;
        left = _left;
        right = _right;
        distance = _dist;
    }

    @Override
    public synchronized void start() {
        super.start();
        System.err.println("start Move Forward");
    }

    @Override
    protected void initialize() {
        super.initialize();
        ahrs.reset();
        System.err.println("initialize Move Forward");
        moveController = new PIDController(0.0095, 0.00, 0.1, 0.00, angleSource, motorSpeedWrite, 0.02);
        moveController.setInputRange(-1000 * Constants.TICKS_PER_REV, 1000 * Constants.TICKS_PER_REV);
        moveController.setOutputRange(-.2, .2);
        moveController.setAbsoluteTolerance(toleranceInches);
        moveController.setContinuous(true);
        moveController.setSetpoint(((-right.getSelectedSensorPosition(0)) + distance * Constants.TICKS_PER_REV));  //fix if given negative val
        moveController.enable();
        moveController.setAbsoluteTolerance(toleranceInches);
        System.err.println("initialize Move Forward");
        SmartDashboard.putNumber("expected dist:", moveController.getSetpoint());
    }

    @Override
    protected void end() {
        System.err.println("end Move Forward");
        moveController.disable();
        super.end();
    }

    @Override
    protected void interrupted() {
        System.err.println("interrupted Move Forward");
        moveController.disable();
        super.interrupted();
    }

    @Override
    protected void execute() {
        super.execute();

        System.err.println("Speed: " + moveSpeed + " Get: " + moveController.get());

        left.set(ControlMode.PercentOutput, moveSpeed);
        right.set(ControlMode.PercentOutput, -moveSpeed);

        SmartDashboard.putNumber("Encoder:", -right.getSelectedSensorPosition(0));


        System.err.println("execute Move Forward");
    }


    @Override
    protected boolean isFinished() {

        double current = -right.getSelectedSensorPosition(0);
        double intended = current + (distance * Constants.TICKS_PER_REV);

        if(Math.abs(Math.abs(current) - Math.abs(intended)) < toleranceInches) {
            moveController.disable();
            return true;
        }

        return false;
    }

}