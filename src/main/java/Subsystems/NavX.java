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

    private static boolean connect() {
        if (navx == null || !navx.isConnected()) {
            try {
                navx = new AHRS(SerialPort.Port.kUSB);
                System.out.println("NavX Connected: " + navx.isConnected());
            } catch (RuntimeException ex) {
                DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
                return false;
            }
        }
        return true;
    }

    public static void dashboardStats() {
        try {
            SmartDashboard.putNumber("NavX/Gyro/Yaw", NavX.getNavx().getYaw());
        } catch (NullPointerException npe) {
            NavX.connect();
        }
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

    public static AHRS getNavx() throws NullPointerException {
        if (NavX.connect()) {
            return navx;
        } else {
            throw new NullPointerException("Failed to connect to the NavX!");
        }
    }
}
