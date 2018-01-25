package AutoModes.Commands;

import Subsystems.DriveTrain;
import Subsystems.NavX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.sun.java.util.jar.pack.DriverResource;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Constants;
import robot.Robot;

public class MoveForward extends Command {

    private static final double WHEEL_SIZE = 4.0 * 3.14;
    private static final double TOLERANCE_TICKS = (Constants.TICKS_PER_REV) / 5;
    private static final double TOLERANCE_DEGREES = 0.5;

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
            return DriveTrain._leftMain.getSelectedSensorPosition(0);

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
            return DriveTrain._rightMain.getSelectedSensorPosition(0);

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
            return NavX.getNavx().getYaw();
        }
    };

    PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            turnSpeed = a;  //change to -a later when .setInverted works

            SmartDashboard.putNumber("Turn Speed", turnSpeed);
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

    public MoveForward(double _dist) {
        requires(Robot.navx);
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
        //NavX.getNavx().zeroYaw();
        System.err.println("initialize Move Forward");

        angle = NavX.getNavx().getYaw();

        moveLeftController = new PIDController(Constants.FORWARD_LEFT_P, Constants.FORWARD_LEFT_I, Constants.FORWARD_LEFT_D, Constants.FORWARD_LEFT_F, leftSource, motorLeftSpeedWrite, Constants.FORWARD_LEFT_PERIOD); //i: 0.000003 d: 0002
        moveLeftController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        moveLeftController.setOutputRange(-0.5, 0.5);
        moveLeftController.setAbsoluteTolerance(TOLERANCE_TICKS);
        moveLeftController.setContinuous(true);
        moveLeftController.setSetpoint(((DriveTrain._leftMain.getSelectedSensorPosition(0)) + targetTicks));
        moveLeftController.enable();

        moveRightController = new PIDController(Constants.FORWARD_RIGHT_P, Constants.FORWARD_RIGHT_I, Constants.FORWARD_RIGHT_D, Constants.FORWARD_RIGHT_F, rightSource, motorRightSpeedWrite, Constants.FORWARD_RIGHT_PERIOD); //i: 0.000003 d: 0002
        moveRightController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        moveRightController.setOutputRange(-0.5, 0.5);
        moveRightController.setAbsoluteTolerance(TOLERANCE_TICKS);
        moveRightController.setContinuous(true);
        moveRightController.setSetpoint(((DriveTrain._rightMain.getSelectedSensorPosition(0)) + targetTicks));
        moveRightController.enable();


        angleController = new PIDController(Constants.FORWARD_ANGLE_P, Constants.FORWARD_ANGLE_I, Constants.FORWARD_ANGLE_D, Constants.FORWARD_ANGLE_F, angleSource, motorSpeedWrite, Constants.FORWARD_ANGLE_PERIOD);

        angleController.setP(SmartDashboard.getNumber("AnglePID/P", 0.05));
        angleController.setI(SmartDashboard.getNumber("AnglePID/I", 0.0));
        angleController.setD(SmartDashboard.getNumber("AnglePID/D", 0.05));
        angleController.setF(SmartDashboard.getNumber("AnglePID/F", 0.0));
        angleController.setEnabled(SmartDashboard.getBoolean("AnglePID/Enabled", true));

        SmartDashboard.putNumber("AnglePID/P", angleController.getP());
        SmartDashboard.putNumber("AnglePID/I", angleController.getI());
        SmartDashboard.putNumber("AnglePID/D", angleController.getD());
        SmartDashboard.putNumber("AnglePID/F", angleController.getF());
        SmartDashboard.putBoolean("AnglePID/Enabled", angleController.isEnabled());

        angleController.setInputRange(-180.0, 180.0);
        angleController.setOutputRange(-0.2, 0.2);
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
        angleController.disable();
        super.end();
    }

    @Override
    protected void interrupted() {
        System.err.println("interrupted Move Forward");
        moveLeftController.disable();
        moveRightController.disable();
        angleController.disable();

        DriveTrain.tankDrive(0, 0);
        super.interrupted();
    }

    @Override
    protected void execute() {
        super.execute();

        if(!angleController.isEnabled()) {
            angleController.enable();
        }

        if(turnSpeed > 0) {
            moveLeftSpeed += Math.abs(turnSpeed);
        } else {
            moveRightSpeed += Math.abs(turnSpeed);
        }

        angleController.setP(SmartDashboard.getNumber("AnglePID/P", angleController.getP()));
        angleController.setI(SmartDashboard.getNumber("AnglePID/I", angleController.getI()));
        angleController.setD(SmartDashboard.getNumber("AnglePID/D", angleController.getD()));
        angleController.setF(SmartDashboard.getNumber("AnglePID/F", angleController.getF()));
        angleController.setEnabled(SmartDashboard.getBoolean("AnglePID/Enabled", angleController.isEnabled()));

        SmartDashboard.putNumber("AnglePID/P", angleController.getP());
        SmartDashboard.putNumber("AnglePID/I", angleController.getI());
        SmartDashboard.putNumber("AnglePID/D", angleController.getD());
        SmartDashboard.putNumber("AnglePID/F", angleController.getF());
        SmartDashboard.putBoolean("AnglePID/Enabled", angleController.isEnabled());


        SmartDashboard.putNumber("Left Error", moveLeftController.getError());
        SmartDashboard.putNumber("Right Error", moveRightController.getError());
        DriveTrain._leftMain.set(ControlMode.PercentOutput, moveLeftSpeed);
        DriveTrain._rightMain.set(ControlMode.PercentOutput, moveRightSpeed);

    }


    @Override
    protected boolean isFinished() {
        //System.err.println("moveLeftController.onTarget(): " + moveLeftController.onTarget() + "moveLeftController.get(): " + moveLeftController.get());
        //System.err.println("moveRightController.onTarget(): " + moveRightController.onTarget() + "moveRightController.get(): " + moveRightController.get());
        //System.err.println("angleController.onTarget(): " + angleController.onTarget() + "angleController.get(): " + angleController.get());

        /*
        if((moveLeftController.get() >= -0.075 && moveLeftController.get() <= 0.075) && (moveRightController.get() >= -0.075 &&
                moveRightController.get() <= 0.075)) {
            moveLeftController.disable();
            moveRightController.disable();
            angleController.disable();

            System.out.println("DISABLE LEFT RIGHT & ANGLE");
            return true;
        }
        */

        if(Math.abs(moveLeftController.getError()) < TOLERANCE_TICKS && Math.abs(moveRightController.getError()) < TOLERANCE_TICKS){
            moveLeftController.disable();
            moveRightController.disable();
            angleController.disable();

            System.out.println("DISABLE LEFT RIGHT & ANGLE");

            DriveTrain.tankDrive(0, 0);
            return true;
        }

        return false;
    }

}