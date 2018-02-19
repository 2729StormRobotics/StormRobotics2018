package Subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
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

            try {
                return connectMXPSPI();
            } catch (RuntimeException ex) {
                // Failed to connect via MXP SPI, trying next option.
            }
            try {
                return connectMXPI2C();
            } catch (RuntimeException ex) {
                // Failed to connect via MXP I2C, trying next option.
            }
            try {
                return connectMXPSerial();
            } catch (RuntimeException ex) {
                // Failed to connect via MXP Serial. Throwing error.
                System.err.println("Failed to connect to NavX on MXP SPI, I2C, or Serial!");
                return false;
            }
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

    private static synchronized boolean connectMXPSPI() throws RuntimeException {
        if (navx == null || !navx.isConnected()) {
            navx = new AHRS(SPI.Port.kMXP);
            System.out.println("NavX Connected: " + navx.isConnected());
        }
        return true;
    }

    private static synchronized boolean connectMXPI2C() throws RuntimeException {
        if (navx == null || !navx.isConnected()) {
            navx = new AHRS(I2C.Port.kMXP);
            System.out.println("NavX Connected: " + navx.isConnected());
        }
        return true;
    }

    private static synchronized boolean connectMXPSerial() throws RuntimeException {
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
            SmartDashboard.putBoolean("NavX/Connected", NavX.getNavx().isConnected());
            SmartDashboard.putNumber("NavX/Gyro/RawX", NavX.getNavx().getRawGyroX());
            SmartDashboard.putNumber("NavX/Gyro/RawY", NavX.getNavx().getRawGyroY());
            SmartDashboard.putNumber("NavX/Gyro/RawZ", NavX.getNavx().getRawGyroZ());
            SmartDashboard.putNumber("NavX/Gyro/Angle", NavX.getNavx().getAngle());
            SmartDashboard.putNumber("NavX/Gyro/AngleAdjustment", NavX.getNavx().getAngleAdjustment());
            SmartDashboard.putNumber("NavX/Gyro/Yaw", NavX.getNavx().getYaw());
            SmartDashboard.putNumber("NavX/Gyro/Pitch", NavX.getNavx().getPitch());
            SmartDashboard.putNumber("NavX/Gyro/Roll", NavX.getNavx().getRoll());
        } catch (NullPointerException npe) {
            SmartDashboard.putBoolean("NavX/Connected", false);
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

    /**
     *
     * @return
     * @throws NullPointerException
     */
    public static synchronized AHRS getNavx() throws NullPointerException {
        if (NavX.connect()) {
            return navx;
        } else {
            throw new NullPointerException("Failed to connect to the NavX!");
        }
    }
}
