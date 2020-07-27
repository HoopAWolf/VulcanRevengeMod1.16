package com.hoopawolf.vrm.helper;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;


public class EntityHelper
{
    public static double getAngleBetweenEntities(Entity first, Entity second)
    {
        return Math.atan2(second.getPosZ() - first.getPosZ(), second.getPosX() - first.getPosX()) * (180 / Math.PI) + 90;
    }

    public static void sendCoolDownMessage(PlayerEntity entityIn, int cooldownIn)
    {
        String message = "";

        if (cooldownIn > 0)
        {
            message = "message.vrm.cooldown";
        } else
        {
            message = "message.vrm.error";
        }

        entityIn.sendMessage(new TranslationTextComponent(I18n.format(message) + ((cooldownIn > 0) ? " " + (cooldownIn / 20) + "s" : "")).mergeStyle(Style.EMPTY.setFormatting(TextFormatting.RED)), entityIn.getUniqueID());
    }

    public static void sendMessage(PlayerEntity entityIn, String messageID, TextFormatting colorIn)
    {
        String message = "message.vrm." + messageID;

        entityIn.sendMessage(new TranslationTextComponent(I18n.format(message)).mergeStyle(Style.EMPTY.setFormatting(colorIn)), entityIn.getUniqueID());
    }

    public static List<PlayerEntity> getPlayersNearby(Entity ent, double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> nearbyEntities = ent.world.getEntitiesWithinAABBExcludingEntity(ent, ent.getBoundingBox().grow(distanceX, distanceY, distanceZ));
        return nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof PlayerEntity && ent.getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (PlayerEntity) entityNeighbor).collect(Collectors.toList());
    }

    public static List<LivingEntity> getEntityLivingBaseNearby(Entity ent, double distanceX, double distanceY, double distanceZ, double radius)
    {
        return getEntitiesNearby(ent, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public static <T extends Entity> List<T> getEntitiesNearby(Entity ent, Class<T> entityClass, double r)
    {
        return ent.world.getEntitiesWithinAABB(entityClass, ent.getBoundingBox().grow(r, r, r), e -> e != ent && ent.getDistance(e) <= r);
    }

    public static <T extends Entity> List<T> getEntitiesNearby(Entity ent, Class<T> entityClass, double dX, double dY, double dZ, double r)
    {
        return ent.world.getEntitiesWithinAABB(entityClass, ent.getBoundingBox().grow(dX, dY, dZ), e -> e != ent && ent.getDistance(e) <= r && e.getPosY() <= ent.getPosY() + dY);
    }

    public static <T extends Entity> List<T> getEntitiesNearbyWithPos(World world, AxisAlignedBB box, BlockPos pos, Class<T> entityClass, double dX, double dY, double dZ, double r)
    {
        return world.getEntitiesWithinAABB(entityClass, box.grow(dX, dY, dZ), e -> pos.withinDistance(e.getPositionVec(), r) && e.getPosY() <= pos.getY() + dY);
    }
}