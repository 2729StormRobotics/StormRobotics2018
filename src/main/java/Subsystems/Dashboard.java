package Subsystems;

import AutoModes.Commands.Lift;
import AutoModes.Commands.MoveForward;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Robot;

public class Dashboard {

    public Dashboard() {
    }

    public static void checkAccel() {
        SmartDashboard.putBoolean("Accel Disable", Robot.accelerationDisable);
    }

    public static void checkPneumatics() { SmartDashboard.putBoolean("Intake Pneumatics", Robot.armControl); }

    public static void checkTurnSpeed() {
        SmartDashboard.putNumber("Turn Speed", MoveForward.turnSpeed);
    }

    public static void sendChooser() {
        SmartDashboard.putData("Test Autonomous Modes", Robot.autoChooser);
        SmartDashboard.putData("Auto Position", Robot.positionChooser);
        SmartDashboard.putData("Auto Preference", Robot.preferenceChooser);
        SmartDashboard.putData("Debug Level", Robot.debugChooser);
    }


    public static void sendElevatorEncoders() {
        SmartDashboard.putNumber("Left Encoder", Elevator._elevatorLeft.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder", Elevator._elevatorRight.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Elevator Speed", Lift.elevatorSpeed);
    }


    public static void sendEncoders() {
        SmartDashboard.putNumber("Encoder Left", DriveTrain._leftMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Left Velocity", DriveTrain._leftMain.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Encoder Right", DriveTrain._rightMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Right Velocity", DriveTrain._rightMain.getSelectedSensorVelocity(0));
    }

    public static void sendMotorControllerInfo(String category, TalonSRX talon) {
        SmartDashboard.putNumber(category + "Bus Voltage", talon.getBusVoltage());
        SmartDashboard.putNumber(category + "Output Percent", talon.getMotorOutputPercent());
        SmartDashboard.putNumber(category + "Output Voltage", talon.getMotorOutputVoltage());
        SmartDashboard.putNumber(category + "Output Current", talon.getOutputCurrent());
        SmartDashboard.putNumber(category + "Output Watts", talon.getOutputCurrent() * talon.getMotorOutputVoltage());
        SmartDashboard.putString(category + "control Mode", talon.getControlMode().toString());
        SmartDashboard.putNumber(category + "Temperature", talon.getTemperature());
        SmartDashboard.putBoolean(category + "Inverted", talon.getInverted());
    }

    public static void sendNavXInfo() {
        if(NavX.getNavx() != null) {
            SmartDashboard.putBoolean("NavX/Connected", NavX.getNavx().isConnected());
            SmartDashboard.putNumber("NavX/Gyro/Pitch", NavX.getNavx().getPitch());
        }
    }


    public static void sendNavXAll() {
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

}
