package AutoModes.Commands;

import robot.Dashboard;
import Subsystems.DriveTrain;
import Subsystems.NavX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import robot.Constants;
import robot.Robot;

public class MoveForward extends Command {

    public static double turnSpeed;
    double moveLeftSpeed, moveRightSpeed, distance, angle;
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

        public double pidGet() { // Encoder Position robot @
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

        public double pidGet() { // Encoder Position robot @
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

        public double pidGet() { // Angle robot at
            return NavX.getNavx().getYaw();
        }
    };

    PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            turnSpeed = a;
            Robot._dashboard.checkTurnSpeed();
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
        //requires(robot.navx);
        distance = _dist;
    }

    private double ticksToInches(double ticks) {
        return (ticks / Constants.TICKS_PER_REV) * Constants.WHEEL_SIZE;
    }

    private double inchesToTicks(double inches) {
        return (inches / Constants.WHEEL_SIZE) * Constants.TICKS_PER_REV;
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

        //angle = NavX.getNavx().getYaw();

        moveLeftController = new PIDController(Constants.FORWARD_LEFT_P, Constants.FORWARD_LEFT_I, Constants.FORWARD_LEFT_D, Constants.FORWARD_LEFT_F, leftSource, motorLeftSpeedWrite, Constants.FORWARD_LEFT_PERIOD); //i: 0.000003 d: 0002
        moveLeftController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        moveLeftController.setOutputRange(-0.5, 0.5);
        moveLeftController.setAbsoluteTolerance(Constants.TOLERANCE_TICKS);
        moveLeftController.setContinuous(true);
        moveLeftController.setSetpoint(((DriveTrain._leftMain.getSelectedSensorPosition(0)) + targetTicks));
        moveLeftController.enable();

        moveRightController = new PIDController(Constants.FORWARD_RIGHT_P, Constants.FORWARD_RIGHT_I, Constants.FORWARD_RIGHT_D, Constants.FORWARD_RIGHT_F, rightSource, motorRightSpeedWrite, Constants.FORWARD_RIGHT_PERIOD); //i: 0.000003 d: 0002
        moveRightController.setInputRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        moveRightController.setOutputRange(-0.5, 0.5);
        moveRightController.setAbsoluteTolerance(Constants.TOLERANCE_TICKS);
        moveRightController.setContinuous(true);
        moveRightController.setSetpoint(((DriveTrain._rightMain.getSelectedSensorPosition(0)) + targetTicks));
        moveRightController.enable();


        angleController = new PIDController(Constants.FORWARD_ANGLE_P, Constants.FORWARD_ANGLE_I, Constants.FORWARD_ANGLE_D, Constants.FORWARD_ANGLE_F, angleSource, motorSpeedWrite, Constants.FORWARD_ANGLE_PERIOD);

        angleController.setInputRange(-180.0, 180.0);
        angleController.setOutputRange(-0.3, 0.3);
        angleController.setAbsoluteTolerance(Constants.TOLERANCE_DEGREES);
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

        if (!angleController.isEnabled()) {
            angleController.enable();
        }

        if (turnSpeed > 0) {
            moveLeftSpeed += Math.abs(turnSpeed);
        } else {
            moveRightSpeed += Math.abs(turnSpeed);
        }

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

        if (Math.abs(moveLeftController.getError()) < Constants.TOLERANCE_TICKS && Math.abs(moveRightController.getError()) < Constants.TOLERANCE_TICKS) {
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
