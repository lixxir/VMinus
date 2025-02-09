package net.lixir.vminus.util;

public class VMath {
    // Returns 0 at end points, peaks at 1 in the middle
    public static double peak(double x, double a, double b) {
        if (a > b) {
            double temp = a;
            a = b;
            b = temp;
        }
        double midpoint = (a + b) / 2.0;
        double range = b - a;
        if (x <= a || x >= b) {
            return 0;
        }
        double distanceToMidpoint = Math.abs(x - midpoint);
        return 1 - (2 * distanceToMidpoint / range);
    }

}
