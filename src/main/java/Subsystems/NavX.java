package Subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavX extends Subsystem {
    private static AHRS navx;

    public NavX() {
        NavX.connect();
    }

    private static void connect() {
        if (navx == null || !navx.isConnected()) {
            try {
                navx = new AHRS(SerialPort.Port.kUSB);
                System.out.println("NavX Connected: " + navx.isConnected());
                SmartDashboard.putBoolean("NavX Connected", NavX.getNavx().isConnected());
            } catch (RuntimeException ex) {
                DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
            }
        }

        if (navx != null) {
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

    public static void dashboardStats() {
        SmartDashboard.putNumber("NavX/Gyro/Yaw", NavX.getNavx().getYaw());
    }

    @Override
    protected void initDefaultCommand() {
        periodic();
    }

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("NavX/Gyro/Yaw",  NavX.getNavx().getYaw());
        NavX.connect();
    }

    public static AHRS getNavx() {
        if (navx == null) {
            NavX.connect();
        }
        return navx;
    }
}
