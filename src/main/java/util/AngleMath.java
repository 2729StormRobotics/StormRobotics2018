package util;

public class AngleMath {
    public static double fixRange(double angle) {
        double temp = angle % 360;
        return Math.abs(temp) <= 180 ? temp : (temp < -180 ? temp + 360:temp - 360);
    }

    public static double add(double angle1, double angle2){
        return fixRange(angle1 + angle2);
    }
}
