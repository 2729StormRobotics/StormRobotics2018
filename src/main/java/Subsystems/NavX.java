package Subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;

public class NavX extends Subsystem {
    private static AHRS navx;

    public NavX() {
        try {
            navx = new AHRS(SPI.Port.kMXP);
            System.out.println("NavX Enabled!");
            System.out.println("NavX Connected: " + navx.isConnected());
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
        }
    }

    private static void connect() {
        if (navx == null || !navx.isConnected()) {
            try {
                navx = new AHRS(SPI.Port.kMXP);
                System.out.println("NavX Connected: " + navx.isConnected());
            } catch (RuntimeException ex ) {
                DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
            }
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

    public static AHRS getNavx() {
        if (navx == null) {
            NavX.connect();
        }
        return navx;
    }
}
