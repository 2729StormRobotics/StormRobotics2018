package Subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavX extends Subsystem {
    private static AHRS navx;

    public NavX() {
        NavX.connect();
    }

    private static synchronized boolean connect() {
        if (navx == null || !navx.isConnected()) {
            /*
            try {
                return connectMXP();
            } catch (RuntimeException ex) {
                // Failed to connect via MXP, trying next option.
            }
            */
            try {
                return connectUSB();
            } catch (RuntimeException ex) {
                // Failed to connect via USB, trying next option.
                System.err.println("Failed to connect to NavX on USB!");
                return false;
            }
            /*
            try {
                return connectI2C();
            } catch (RuntimeException ex) {
                // Failed to connect via i2c. Throwing error.
                System.err.println("Failed to connect to NavX on MXP, USB, or I2C!");
                return false;
            }
            */
        }

        return true;
    }

    private static synchronized boolean connectUSB() throws RuntimeException {
        if (navx == null || !navx.isConnected()) {
            navx = new AHRS(SerialPort.Port.kUSB);
            System.out.println("NavX Connected: " + navx.isConnected());
        }
        return true;
    }

    private static synchronized boolean connectMXP() throws RuntimeException {
        if (navx == null || !navx.isConnected()) {
            navx = new AHRS(SerialPort.Port.kMXP);
            System.out.println("NavX Connected: " + navx.isConnected());
        }
        return true;
    }

    private static synchronized boolean connectI2C() throws RuntimeException {
        if (navx == null || !navx.isConnected()) {
            navx = new AHRS(I2C.Port.kOnboard);
            System.out.println("NavX Connected: " + navx.isConnected());
        }
        return true;
    }

    public static synchronized void dashboardStats() {
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

    public static synchronized AHRS getNavx() throws NullPointerException {
        if (NavX.connect()) {
            return navx;
        } else {
            throw new NullPointerException("Failed to connect to the NavX!");
        }
    }
}
