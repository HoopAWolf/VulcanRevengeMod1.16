package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.potion.PotionEffectBase;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionRegistryHandler
{
    public static final DeferredRegister<Potion> POTION = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Reference.MOD_ID);
    public static final DeferredRegister<Effect> POTION_EFFECT = DeferredRegister.create(ForgeRegistries.POTIONS, Reference.MOD_ID);

    //EFFECTS
    public static final RegistryObject<Effect> PLAGUE_EFFECT = POTION_EFFECT.register("plagueeffect", () -> new PotionEffectBase(EffectType.HARMFUL, 3035801));
    public static final RegistryObject<Effect> DAZED_EFFECT = POTION_EFFECT.register("dazedeffect", () -> new PotionEffectBase(EffectType.HARMFUL, 5578058).addAttributesModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -7.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));

    //POTION
    public static final RegistryObject<Potion> PLAGUE_POTION = POTION.register("plaguepotion", () -> new Potion(new EffectInstance(PLAGUE_EFFECT.get(), 1000)));
    public static final RegistryObject<Potion> DAZED_POTION = POTION.register("dazedeffect", () -> new Potion(new EffectInstance(DAZED_EFFECT.get(), 1000)));

    public static void init(IEventBus _iEventBus)
    {
        POTION_EFFECT.register(_iEventBus);
        POTION.register(_iEventBus);
    }
}
