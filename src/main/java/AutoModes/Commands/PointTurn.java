package AutoModes.Commands;

import Subsystems.NavX;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import robot.Constants;
import robot.Robot;

public class PointTurn extends Command {
    private double turnSpeed;
    private final double targetAngle;
    private PIDController turnController;

    /**
     * Turns robot to desired angle relative to current position
     * @param angle clockwise angle in degrees to rotate.  Range (-180, 180)
     */
    public PointTurn(double angle) { //Accepts only values between (-180, 180)
        targetAngle = angle;
    }

    private final PIDSource angleSource = new PIDSource() {
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
            try {
                return NavX.getNavx().getYaw();
            } catch (NullPointerException npe) {
                return targetAngle;
            }
        }
    };

    private final PIDOutput motorSpeedWrite = new PIDOutput() {
        public void pidWrite(double a) {
            //System.out.println("PID output: " + a);
            turnSpeed = a;  //change to -a later when .setInverted works
        }
    };

    /**
     * Sets up PID Controller for point turn.  Accounts for ranges > 180 and < -180
     */
    @Override
    protected void initialize() {
        super.initialize();

        turnController = new PIDController(Constants.TURNCONTROLLER_P, Constants.TURNCONTROLLER_I, Constants.TURNCONTROLLER_D, Constants.TURNCONTROLLER_F, angleSource, motorSpeedWrite, Constants.TURNCONTROLLER_PERIOD);
        turnController.setInputRange(-180.0, 180.0);
        turnController.setOutputRange(-.80, .80);
        turnController.setAbsoluteTolerance(Constants.POINT_TURN_TOLERANCE);
        turnController.setContinuous(true);
        double setpoint;
        double currentYaw = 0;
        try{
            currentYaw = NavX.getNavx().getYaw();
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
        setpoint = targetAngle + currentYaw;


        if (setpoint > 180)
            setpoint = currentYaw + targetAngle - 360;
        else if (setpoint < -180)
            setpoint = currentYaw - targetAngle + 360;

        turnController.setSetpoint(setpoint);
        System.out.println("Starting At: " + currentYaw);
        System.out.println("Starting with setpoint: " + turnController.getSetpoint());
        turnController.enable();
        System.err.println("start Point Turn");
    }

    /**
     * After Command is finished turn off drive motors
     * @see Command#end()
     */
    @Override
    protected void end() {
        super.end();
        System.err.println("end Point Turn");
        turnController.disable();
        //turnController = null;
        Robot._driveTrain.tankDrive(0, 0);
    }

    /**
     * If interrupted call end
     * @see Command#interrupted()
     */
    @Override
    protected void interrupted() {
        super.interrupted();
        System.err.println("interrupted Point Turn");
        end();
    }

    /**
     * Sets motors to newly calculated turn speed
     * @see Command#execute()
     */
    @Override
    protected void execute() {
        super.execute();
        turnController.getDeltaSetpoint();

        Robot._driveTrain.tankDrive(turnSpeed, -turnSpeed, false, 0);
    }

    /**
     * Once gyro returns gyro within acceptable range turn off motors.
     * @return true means finished.  False means to call execute again
     * @see Command#isFinished()
     */
    @Override
    protected boolean isFinished() {
        if (Math.abs(turnController.getError()) < Constants.POINT_TURN_TOLERANCE) {
            turnController.disable();
            Robot._driveTrain.tankDrive(0, 0);
            System.err.println("finish Point Turn");
            return true;
        }
        return false;
    }
}
