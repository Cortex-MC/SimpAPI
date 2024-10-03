package io.myzticbean.mcdevtools.math;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {

    public static Double calculateDistance2D(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    public static Double calculateDistance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.pow((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2)), 0.5);
    }
}
