package com.hoopawolf.vrm.helper;

import com.hoopawolf.vrm.entities.SlothPetEntity;
import net.minecraft.block.BushBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class RayTracingHelper
{
    public static final RayTracingHelper INSTANCE = new RayTracingHelper();
    private final Minecraft mc = Minecraft.getInstance();
    private Entity target = null;
    private BlockPos endPos;

    private RayTracingHelper()
    {
    }

    public void fire()
    {
        if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY)
        {
            this.target = (Entity) mc.objectMouseOver.hitInfo;
            return;
        }

        Entity viewpoint = mc.getRenderViewEntity();
        if (viewpoint == null)
            return;

        this.target = this.rayTrace(viewpoint, mc.playerController.getBlockReachDistance() * 10, 0);
    }

    public Entity rayTrace(Entity entity, double playerReach, float partialTicks)
    {
        Vector3d eyePosition = entity.getEyePosition(partialTicks);
        Vector3d lookVector = entity.getLook(partialTicks);
        Vector3d traceStart = eyePosition.add(lookVector.x, lookVector.y, lookVector.z);
        Vector3d traceEnd = eyePosition.add(lookVector.x * playerReach, lookVector.y * playerReach, lookVector.z * playerReach);
        traceStart = new Vector3d((int) traceStart.getX(), (int) traceStart.getY(), (int) traceStart.getZ());
        traceEnd = new Vector3d((int) traceEnd.getX(), (int) traceEnd.getY(), (int) traceEnd.getZ());
        Vector3d checkPos = traceStart;
        float percentage = 0.0F;
        endPos = null;

        while (checkPos.getX() != traceEnd.getX() || checkPos.getY() != traceEnd.getY() || checkPos.getZ() != traceEnd.getZ())
        {
            //Reference.LOGGER.debug("Start: " + traceStart + " Checking curr pos: " + checkPos + " with ending of: " + traceEnd + " at percentage: " + percentage);

            if (entity.world.getBlockState(new BlockPos(checkPos)).isSolid() && !(entity.world.getBlockState(new BlockPos(checkPos)).getBlock() instanceof BushBlock))
            {
                endPos = new BlockPos(checkPos);
                // Reference.LOGGER.debug("Hit something solid: " + entity.world.getBlockState(new BlockPos(checkPos)));
                break;
            }

            for (Entity hit : entity.world.getEntitiesInAABBexcluding(null,
                    new AxisAlignedBB(checkPos.getX(), checkPos.getY(), checkPos.getZ(), checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1),
                    e -> e != entity && !(e instanceof SlothPetEntity)))
            {
                // Reference.LOGGER.debug("Got something WOO: " + hit);
                endPos = hit.getPosition();
                return hit;
            }
            percentage += 0.01F;
            checkPos = VRMMathHelper.Lerp(traceStart, traceEnd, MathHelper.clamp(percentage, 0.0F, 1.0F));
        }
        // Reference.LOGGER.debug("Didn't get shit");
        return null;
    }

    public Entity getTarget()
    {
        return this.target;
    }

    public BlockPos getFinalPos()
    {
        return this.endPos;
    }
}