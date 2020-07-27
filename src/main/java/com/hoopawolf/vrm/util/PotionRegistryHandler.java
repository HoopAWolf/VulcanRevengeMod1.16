package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.potion.PotionEffectBase;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionRegistryHandler
{
    public static final DeferredRegister<Effect> POTION_EFFECT = DeferredRegister.create(ForgeRegistries.POTIONS, Reference.MOD_ID);

    //EFFECTS
    public static final RegistryObject<Effect> PLAGUE_EFFECT = POTION_EFFECT.register("plagueeffect", () -> new PotionEffectBase(EffectType.HARMFUL, 3035801));
    public static final RegistryObject<Effect> DAZED_EFFECT = POTION_EFFECT.register("dazedeffect", () -> new PotionEffectBase(EffectType.HARMFUL, 5578058).addAttributesModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -7.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Effect> FEAR_EFFECT = POTION_EFFECT.register("feareffect", () -> new PotionEffectBase(EffectType.HARMFUL, 3012801));

    public static void init(IEventBus _iEventBus)
    {
        POTION_EFFECT.register(_iEventBus);
    }
}
