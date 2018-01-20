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

    private static final double WHEEL_SIZE = 4.0 * 3.14;
    private static final double TOLERANCE_TICKS = (Constants.TICKS_PER_REV) / 50;


    AHRS ahrs;
    TalonSRX left, right;
    double moveLeftSpeed, moveRightSpeed, distance;
    PIDController moveLeftController, moveRightController;

    PIDSource leftSource = new PIDSource() {
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
            return left.getSelectedSensorPosition(0);

        }
    };

    PIDSource rightSource = new PIDSource() {
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
            return right.getSelectedSensorPosition(0);

        }
    };

    PIDOutput motorLeftSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            moveLeftSpeed = a;
            SmartDashboard.putNumber("MoveForward Output:", a);
        }
    };

    PIDOutput motorRightSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            moveRightSpeed = a;
            SmartDashboard.putNumber("MoveForward Output:", a);
        }
    };

    public MoveForward(AHRS _ahrs, double _dist, TalonSRX _left, TalonSRX _right) {
        ahrs = _ahrs;
        left = _left;
        right = _right;
        distance = _dist;
    }

    private double ticksToInches(double ticks) {
        return (ticks / Constants.TICKS_PER_REV) * WHEEL_SIZE;
    }
    private double inchesToTicks(double inches) {
        return (inches / WHEEL_SIZE) * Constants.TICKS_PER_REV;
    }

    @Override
    public synchronized void start() {
        super.start();
        System.err.println("start Move Forward");
    }

    @Override
    protected void initialize() {
        super.initialize();
        double targetTicks = inchesToTicks(distance);
        ahrs.reset();
        System.err.println("initialize Move Forward");

        moveLeftController = new PIDController(0.0002, 0.0, 0.0002, 0.00, leftSource, motorLeftSpeedWrite, 0.02); //i: 0.000003 d: 0002
        moveLeftController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        moveLeftController.setOutputRange(-.5, .5);
        moveLeftController.setAbsoluteTolerance(TOLERANCE_TICKS);
        moveLeftController.setContinuous(true);
        moveLeftController.setSetpoint(((left.getSelectedSensorPosition(0)) + targetTicks));
        moveLeftController.enable();

        moveRightController = new PIDController(0.0002, 0.0, 0.0002, 0.00, rightSource, motorRightSpeedWrite, 0.02); //i: 0.000003 d: 0002
        moveRightController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        moveRightController.setOutputRange(-.5, .5);
        moveRightController.setAbsoluteTolerance(TOLERANCE_TICKS);
        moveRightController.setContinuous(true);
        moveRightController.setSetpoint(((right.getSelectedSensorPosition(0)) + targetTicks));
        moveRightController.enable();
    }

    @Override
    protected void end() {
        System.err.println("end Move Forward");
        moveLeftController.disable();
        moveRightController.disable();
        super.end();
    }

    @Override
    protected void interrupted() {
        System.err.println("interrupted Move Forward");
        moveLeftController.disable();
        moveRightController.disable();
        super.interrupted();
    }

    @Override
    protected void execute() {
        super.execute();

        left.set(ControlMode.PercentOutput, moveLeftSpeed);
        right.set(ControlMode.PercentOutput, moveRightSpeed);

        SmartDashboard.putNumber("Left Encoder:", left.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder:", right.getSelectedSensorPosition(0));


        System.err.println("execute Move Forward");
    }


    @Override
    protected boolean isFinished() {

        /*
        if(Math.abs(Math.abs(current) - Math.abs(intended)) < TOLERANCE_TICKS) {
            moveController.disable();
            left.set(ControlMode.PercentOutput, 0);
            right.set(ControlMode.PercentOutput, 0);
            return true;
        }
        */



        if((moveLeftController.get() >= -0.05 && moveLeftController.get() <= 0.05 && moveLeftController.onTarget()) && (moveRightController.get() >= -0.05 &&
                moveRightController.get() <= 0.05 && moveRightController.onTarget())) {
            moveLeftController.disable();
            moveRightController.disable();
            return true;
        }




        return false;
    }

}