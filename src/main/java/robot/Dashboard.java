package robot;

import AutoModes.Commands.Lift;
import AutoModes.Commands.MoveForward;
import AutoModes.Commands.PointTurn;
import AutoModes.Modes.*;
import Subsystems.DriveTrain;
import Subsystems.Elevator;
import Subsystems.NavX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.AutoPosition;
import util.AutoPreference;
import util.DebugLevel;

public class Dashboard {

    private static DebugLevel bug;

    SendableChooser<Command> autoChooser;
    SendableChooser<AutoPosition> positionChooser;
    SendableChooser<AutoPreference> preferenceChooser;
    SendableChooser<DebugLevel> debugChooser;

    Dashboard() {
    }

    private void checkAccel() {
        SmartDashboard.putBoolean("Accel Disable", Robot._controller.getSmoothAccel());
    }

    void checkBug() {
        String s = "Info";
        if(bug != null && bug.getName() != null) {
            s = bug.getName();
        }
        switch(s) {
            case "Info":
                this.sendEncoders();
                this.sendNavXInfo();
                break;
            case "Debug":
                this.sendEncoders();
                this.sendElevatorEncoders();
                this.checkAccel();
                this.checkPneumatics();
                this.sendMotorControllerInfo("Motor/right/main/", DriveTrain._rightMain);
                this.sendMotorControllerInfo("Motor/left/main/", DriveTrain._leftMain);
                break;
            case "All":
                this.sendEncoders();
                this.sendElevatorEncoders();
                this.sendNavXAll();
                this.checkAccel();
                this.checkPneumatics();
                this.checkTurnSpeed();
                this.sendMotorControllerInfo("Motor/right/main/", DriveTrain._rightMain);
                this.sendMotorControllerInfo("Motor/right/2/", DriveTrain._right2);
                this.sendMotorControllerInfo("Motor/left/main/", DriveTrain._leftMain);
                this.sendMotorControllerInfo("Motor/left/2/", DriveTrain._left2);
                break;
            default: break;
        }
    }

    private void checkPneumatics() {
        SmartDashboard.putBoolean("Intake Pneumatics", Robot._controller.getIntake());
    }


    public void checkTurnSpeed() {
        SmartDashboard.putNumber("Turn Speed", MoveForward.turnSpeed);
    }

    void sendChooser() {
        autoChooser = new SendableChooser<>();
        autoChooser.addDefault(Constants.POINT_TURN, new PointTurn(90));
        autoChooser.addObject(Constants.MID_SWITCH, new MidSwitch('L'));
        autoChooser.addObject(Constants.LEFT_SWITCH, new LeftSwitch());
        autoChooser.addObject(Constants.RIGHT_SWITCH, new RightSwitch());
        autoChooser.addObject(Constants.LEFT_SCALE, new LeftScale());
        autoChooser.addObject(Constants.RIGHT_SCALE, new RightScale());
        autoChooser.addObject(Constants.POINT_TURN, new PointTurn(90));
        autoChooser.addObject(Constants.MOVE_FORWARD, new MoveForward(262)); //change distance
        autoChooser.addObject(Constants.TEST_MODE, new TestMode());
        autoChooser.addObject(Constants.FOLLOW_PREF, new DummyCommand());

        positionChooser = new SendableChooser<>();
        positionChooser.addDefault(AutoPosition.MIDDLE.getName(), AutoPosition.MIDDLE);
        positionChooser.addObject(AutoPosition.LEFT.getName(), AutoPosition.LEFT);
        positionChooser.addObject(AutoPosition.MIDDLE.getName(), AutoPosition.MIDDLE);
        positionChooser.addObject(AutoPosition.RIGHT.getName(), AutoPosition.RIGHT);

        preferenceChooser = new SendableChooser<>();
        preferenceChooser.addDefault(AutoPreference.SWITCH.getName(), AutoPreference.SWITCH);
        preferenceChooser.addObject(AutoPreference.SWITCH.getName(), AutoPreference.SWITCH);
        preferenceChooser.addObject(AutoPreference.SCALE.getName(), AutoPreference.SCALE);

        debugChooser = new SendableChooser<>();
        debugChooser.addDefault(DebugLevel.INFO.getName(), DebugLevel.INFO);
        debugChooser.addObject(DebugLevel.INFO.getName(), DebugLevel.INFO);
        debugChooser.addObject(DebugLevel.DEBUG.getName(), DebugLevel.DEBUG);
        debugChooser.addObject(DebugLevel.ALL.getName(), DebugLevel.ALL);

        SmartDashboard.putData("Test Autonomous Modes", autoChooser);
        SmartDashboard.putData("Auto Position", positionChooser);
        SmartDashboard.putData("Auto Preference", preferenceChooser);
        SmartDashboard.putData("Debug Level", debugChooser);
    }


    public void sendElevatorEncoders() {
        SmartDashboard.putNumber("Left Encoder", Elevator._elevatorLeft.getSelectedSensorPosition(0));
        //SmartDashboard.putNumber("Right Encoder", Elevator._elevatorRight.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Elevator Speed", Lift.elevatorSpeed);
    }


    private void sendEncoders() {
        SmartDashboard.putNumber("Encoder Left", DriveTrain._leftMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Left Velocity", DriveTrain._leftMain.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Encoder Right", DriveTrain._rightMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Right Velocity", DriveTrain._rightMain.getSelectedSensorVelocity(0));
    }

    private void sendMotorControllerInfo(String category, TalonSRX talon) {
        SmartDashboard.putNumber(category + "Bus Voltage", talon.getBusVoltage());
        SmartDashboard.putNumber(category + "Output Percent", talon.getMotorOutputPercent());
        SmartDashboard.putNumber(category + "Output Voltage", talon.getMotorOutputVoltage());
        SmartDashboard.putNumber(category + "Output Current", talon.getOutputCurrent());
        SmartDashboard.putNumber(category + "Output Watts", talon.getOutputCurrent() * talon.getMotorOutputVoltage());
        SmartDashboard.putString(category + "control Mode", talon.getControlMode().toString());
        SmartDashboard.putNumber(category + "Temperature", talon.getTemperature());
        SmartDashboard.putBoolean(category + "Inverted", talon.getInverted());
    }

    private void sendNavXInfo() {
        if(NavX.getNavx() != null) {
            SmartDashboard.putBoolean("NavX/Connected", NavX.getNavx().isConnected());
            SmartDashboard.putNumber("NavX/Gyro/Pitch", NavX.getNavx().getPitch());
        }
    }


    private void sendNavXAll() {
        if (NavX.getNavx() != null) {
            SmartDashboard.putBoolean("NavX/Connected", NavX.getNavx().isConnected());
            SmartDashboard.putNumber("NavX/Gyro/Pitch", NavX.getNavx().getPitch());
            SmartDashboard.putNumber("NavX/Gyro/Roll", NavX.getNavx().getRoll());
            SmartDashboard.putNumber("NavX/Gyro/Yaw", NavX.getNavx().getYaw());
            SmartDashboard.putNumber("NavX/Altitude", NavX.getNavx().getAltitude());
            SmartDashboard.putNumber("NavX/Displacement/X", NavX.getNavx().getDisplacementX());
            SmartDashboard.putNumber("NavX/Displacement/Y", NavX.getNavx().getDisplacementY());
            SmartDashboard.putNumber("NavX/Displacement/Z", NavX.getNavx().getDisplacementZ());
            SmartDashboard.putNumber("NavX/CompassHeading", NavX.getNavx().getCompassHeading());
            SmartDashboard.putNumber("NavX/Velocity/X", NavX.getNavx().getVelocityX());
            SmartDashboard.putNumber("NavX/Velocity/Y", NavX.getNavx().getVelocityY());
            SmartDashboard.putNumber("NavX/Velocity/Z", NavX.getNavx().getVelocityZ());
            SmartDashboard.putNumber("NavX/BarometricPressure", NavX.getNavx().getBarometricPressure());
            SmartDashboard.putNumber("NavX/Quaternion/W", NavX.getNavx().getQuaternionW());
            SmartDashboard.putNumber("NavX/Quaternion/X", NavX.getNavx().getQuaternionX());
            SmartDashboard.putNumber("NavX/Quaternion/Y", NavX.getNavx().getQuaternionY());
            SmartDashboard.putNumber("NavX/Quaternion/Z", NavX.getNavx().getQuaternionZ());
        }
    }

    void setBug(DebugLevel lvl) {
        bug = lvl;
    }

}
