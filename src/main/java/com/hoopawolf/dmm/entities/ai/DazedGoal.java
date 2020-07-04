package com.hoopawolf.dmm.entities.ai;

import com.hoopawolf.dmm.util.PotionRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DazedGoal extends Goal
{
    private final EnumSet<Goal.Flag> flag = EnumSet.noneOf(Goal.Flag.class);
    private final LivingEntity host;

    public DazedGoal(LivingEntity _host)
    {
        host = _host;
        flag.add(Flag.MOVE);
        flag.add(Flag.JUMP);
        flag.add(Flag.LOOK);
        flag.add(Flag.TARGET);

        this.setMutexFlags(flag);
    }

    @Override
    public boolean shouldExecute()
    {
        return host.isPotionActive(PotionRegistryHandler.DAZED_EFFECT.get());
    }
}
