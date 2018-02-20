package Subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP extends Subsystem {

    private static final int PDP_CHANNELS = 16;
    private static final PowerDistributionPanel pdp = new PowerDistributionPanel();

    @Override
    protected void initDefaultCommand() {

    }

    public static void dashboardStats() {
        SmartDashboard.putString("PDP/.type", "PowerDistributionPanel");
        SmartDashboard.putNumber("PDP/TotalCurrent", 0);
        SmartDashboard.putNumber("PDP/Voltage", pdp.getVoltage());
        SmartDashboard.putNumber("PDP/TotalEnergy", pdp.getTotalEnergy());
        SmartDashboard.putNumber("PDP/TotalPower", pdp.getTotalPower());
        SmartDashboard.putNumber("PDP/Temperature", pdp.getTemperature());

        for (int i = 0; i < PDP_CHANNELS; i++) {
            dashboardPdpChannel(i);
        }

    }

    private static void dashboardPdpChannel(int channel) {

        SmartDashboard.putNumber("PDP/Chan" + channel, pdp.getCurrent(channel));

    }
}
