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
    private static final double TOLERANCE_DEGREES = 0.01;

    AHRS ahrs;
    TalonSRX left, right;
    double moveLeftSpeed, moveRightSpeed, turnSpeed, distance, angle;
    PIDController moveLeftController, moveRightController, angleController;

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

        public double pidGet() { // Angle Robot at
            SmartDashboard.putBoolean("NavX Connected", ahrs.isConnected());
            double a = ahrs.getYaw();
            SmartDashboard.putNumber("Angle", a);
            SmartDashboard.putNumber("Yaw", a);
            SmartDashboard.putNumber("Pitch", ahrs.getPitch());
            SmartDashboard.putNumber("Roll", ahrs.getRoll());
            SmartDashboard.updateValues();
            return a;
        }
    };

    PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            turnSpeed = a;  //change to -a later when .setInverted works
            System.err.println("Turn Speed: " + turnSpeed);
            SmartDashboard.putNumber("Turn Speed", turnSpeed);
            SmartDashboard.updateValues();
        }
    };

    PIDOutput motorLeftSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            moveLeftSpeed = a;
        }
    };

    PIDOutput motorRightSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            moveRightSpeed = a;
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


        angle = ahrs.getYaw();

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

        // 0.005
        // 0.5
        angleController = new PIDController(1.0, 0.00, 0.5, 0.00, angleSource, motorSpeedWrite, 0.02);
        angleController.setInputRange(-180.0, 180.0);
        angleController.setOutputRange(-0.5, 0.5);
        angleController.setAbsoluteTolerance(TOLERANCE_DEGREES);
        angleController.setContinuous(true);
        angleController.setSetpoint(angle);
        angleController.enable();
        System.err.println("angleController enabled");
        System.err.println("Starting Angle: " + angle + ", Setpoint: " + angle);
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

        if(!angleController.isEnabled()) {
            angleController.enable();
            System.err.println("angleController enabled again");
        }

        if(turnSpeed > 0) {
            moveLeftSpeed += Math.abs(turnSpeed);
        } else {
            moveRightSpeed += Math.abs(turnSpeed);
        }

        left.set(ControlMode.PercentOutput, moveLeftSpeed);
        right.set(ControlMode.PercentOutput, moveRightSpeed);

        SmartDashboard.putNumber("Left Encoder", left.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder", right.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("MoveForward moveLeftSpeed", moveLeftSpeed);
        SmartDashboard.putNumber("MoveForward moveRightSpeed", moveRightSpeed);
        SmartDashboard.putNumber("Turn Speed", turnSpeed);
        SmartDashboard.putNumber("Angle", ahrs.getYaw());

        SmartDashboard.putBoolean("Left PID Enabled", moveLeftController.isEnabled());
        SmartDashboard.putBoolean("Right PID Enabled", moveRightController.isEnabled());
        SmartDashboard.putBoolean("Angle PID Enabled", angleController.isEnabled());
        SmartDashboard.updateValues();

    }


    @Override
    protected boolean isFinished() {
        SmartDashboard.putBoolean("Left PID Enabled", moveLeftController.isEnabled());
        SmartDashboard.putBoolean("Right PID Enabled", moveRightController.isEnabled());
        SmartDashboard.putBoolean("Angle PID Enabled", angleController.isEnabled());
        SmartDashboard.updateValues();

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
            //angleController.disable();
            return true;
        }


        SmartDashboard.putBoolean("Left PID Enabled", moveLeftController.isEnabled());
        SmartDashboard.putBoolean("Right PID Enabled", moveRightController.isEnabled());
        SmartDashboard.putBoolean("Angle PID Enabled", angleController.isEnabled());
        SmartDashboard.updateValues();


        return false;
    }

}