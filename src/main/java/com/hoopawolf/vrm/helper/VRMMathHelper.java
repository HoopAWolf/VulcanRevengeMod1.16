package com.hoopawolf.vrm.helper;

import net.minecraft.util.math.vector.Vector3d;

public class VRMMathHelper
{
    public static Vector3d Lerp(Vector3d start, Vector3d end, float percent)
    {
        return (start.add(end.subtract(start).mul(percent, percent, percent)));
    }
}
