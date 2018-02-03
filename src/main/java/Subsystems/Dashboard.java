package Subsystems;

import AutoModes.Commands.Lift;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Robot;

public class Dashboard {

    public Dashboard() {
    }

    public static void sendChooser() {
        SmartDashboard.putData("Test Autonomous Modes", Robot.autoChooser);
        SmartDashboard.putData("Auto Position", Robot.positionChooser);
        SmartDashboard.putData("Auto Preference", Robot.preferenceChooser);
    }

    public static void sendEncoders() {
        SmartDashboard.putNumber("Encoder Left", DriveTrain._leftMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Left Velocity", DriveTrain._leftMain.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Encoder Right", DriveTrain._rightMain.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Encoder Right Velocity", DriveTrain._rightMain.getSelectedSensorVelocity(0));
    }

    public static void checkAccel() {
        SmartDashboard.putBoolean("Accel Disable", Robot.accelerationDisable);
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

    public static void sendElevatorEncoders() {
        SmartDashboard.putNumber("Left Encoder", Elevator._elevatorLeft.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Encoder", Elevator._elevatorRight.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Elevator Speed", Lift.elevatorSpeed);
    }

}
