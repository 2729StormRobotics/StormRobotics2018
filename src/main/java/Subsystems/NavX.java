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
            } catch (RuntimeException ex) {
                DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
            }
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
        NavX.connect();
    }

    public static AHRS getNavx() {
        if (navx == null) {
            NavX.connect();
        }
        return navx;
    }
}
