package com.hoopawolf.vrm.helper;

import net.minecraft.util.math.vector.Vector3d;

public class VRMMathHelper
{
    public static Vector3d Lerp(Vector3d start, Vector3d end, float percent)
    {
        return (start.add(end.subtract(start).mul(percent, percent, percent)));
    }

    public static Vector3d crossProduct(Vector3d vec_A, Vector3d vec_B)
    {
        return new Vector3d(Math.signum((int) (vec_A.getY() * vec_B.getZ() - vec_A.getZ() * vec_B.getY())),
                Math.signum((int) (vec_A.getZ() * vec_B.getX() - vec_A.getX() * vec_B.getZ())),
                Math.signum((int) (vec_A.getX() * vec_B.getY() - vec_A.getY() * vec_B.getX())));
    }

    public static double signum(double d)
    {
        if (d > 0F && d <= 0.5F)
            return 0;
        else if (d > 0.5F)
            return 1;
        else if (d > -0.5F && d < 0F)
            return 0;
        else if (d < -0.5F)
            return -1;

        return 0;
    }
}
