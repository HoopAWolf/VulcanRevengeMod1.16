package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.potion.*;
import com.hoopawolf.vrm.potion.sin.EnvyTrialEffect;
import com.hoopawolf.vrm.potion.sin.PrideTrialEffect;
import com.hoopawolf.vrm.potion.sin.SlothTrialEffect;
import com.hoopawolf.vrm.potion.sin.WrathTrialEffect;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionRegistryHandler
{
    public static final DeferredRegister<Effect> POTION_EFFECT = DeferredRegister.create(ForgeRegistries.POTIONS, Reference.MOD_ID);
    public static final DeferredRegister<Potion> POTION = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Reference.MOD_ID);

    //EFFECTS
    public static final RegistryObject<Effect> PLAGUE_EFFECT = POTION_EFFECT.register("plagueeffect", () -> new PotionEffectBase(EffectType.HARMFUL, 3035801));
    public static final RegistryObject<Effect> DAZED_EFFECT = POTION_EFFECT.register("dazedeffect", () -> new PotionEffectBase(EffectType.HARMFUL, 5578058).addAttributesModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -7.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Effect> FEAR_EFFECT = POTION_EFFECT.register("feareffect", () -> new PotionEffectBase(EffectType.HARMFUL, 3012801));
    public static final RegistryObject<Effect> FLIGHT_EFFECT = POTION_EFFECT.register("flighteffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 3012801));
    public static final RegistryObject<Effect> ECHO_LOCATION_EFFECT = POTION_EFFECT.register("echolocationeffect", () -> new EchoLocationEffect(EffectType.BENEFICIAL, 2012801));
    public static final RegistryObject<Effect> POISON_ATTACK_EFFECT = POTION_EFFECT.register("poisonattackeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 4012801));
    public static final RegistryObject<Effect> FLAME_CHARGE_EFFECT = POTION_EFFECT.register("flamechargeeffect", () -> new FireChargeEffect(EffectType.BENEFICIAL, 1012801));
    public static final RegistryObject<Effect> EXPLOSIVE_FLAME_CHARGE_EFFECT = POTION_EFFECT.register("explosivefirechargeeffect", () -> new ExplosiveFireChargeEffect(EffectType.BENEFICIAL, 1012801));
    public static final RegistryObject<Effect> FALL_RESISTANCE_EFFECT = POTION_EFFECT.register("fallresisteffect", () -> new FallResistanceEffect(EffectType.BENEFICIAL, 3232801));
    public static final RegistryObject<Effect> POISON_RESISTANCE_EFFECT = POTION_EFFECT.register("poisonresisteffect", () -> new PoisonResistanceEffect(EffectType.BENEFICIAL, 4122801));
    public static final RegistryObject<Effect> CLIMB_EFFECT = POTION_EFFECT.register("climbeffect", () -> new ClimbEffect(EffectType.BENEFICIAL, 3442801));
    public static final RegistryObject<Effect> EGG_ATTACK_EFFECT = POTION_EFFECT.register("eggattackeffect", () -> new EggAttackEffect(EffectType.BENEFICIAL, 3652801));
    public static final RegistryObject<Effect> SWIMMING_SPEED_EFFECT = POTION_EFFECT.register("swimmingspeedeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 1112801).addAttributesModifier(ForgeMod.SWIM_SPEED.get(), "9898b50a-d0b6-11ea-87d0-0242ac130003", 1.5F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Effect> MILK_EFFECT = POTION_EFFECT.register("milkeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 4212801));
    public static final RegistryObject<Effect> RAGE_EFFECT = POTION_EFFECT.register("rageeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 4123801));
    public static final RegistryObject<Effect> EXPLOSIVE_RESISTANCE_EFFECT = POTION_EFFECT.register("explosiveresisteffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 4451801));
    public static final RegistryObject<Effect> HUNGER_RESISTANCE_EFFECT = POTION_EFFECT.register("hungerresisteffect", () -> new HungerResistanceEffect(EffectType.BENEFICIAL, 4321801));
    public static final RegistryObject<Effect> TELEPORTATION_EFFECT = POTION_EFFECT.register("teleportationeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 1253801));
    public static final RegistryObject<Effect> EVOKE_EFFECT = POTION_EFFECT.register("evokeeffect", () -> new EvokeEffect(EffectType.BENEFICIAL, 2513801));
    public static final RegistryObject<Effect> STEW_EFFECT = POTION_EFFECT.register("steweffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 2513801));
    public static final RegistryObject<Effect> SLEEP_EFFECT = POTION_EFFECT.register("sleepeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 2533801));
    public static final RegistryObject<Effect> ARROW_ATTACK_EFFECT = POTION_EFFECT.register("arrowattackeffect", () -> new BowAttackEffect(EffectType.BENEFICIAL, 5513801));
    public static final RegistryObject<Effect> INK_EFFECT = POTION_EFFECT.register("inkeffect", () -> new InkEffect(EffectType.BENEFICIAL, 4413801));
    public static final RegistryObject<Effect> FROST_WALK_EFFECT = POTION_EFFECT.register("frostwalkeffect", () -> new FrostWalkEffect(EffectType.BENEFICIAL, 4413801));
    public static final RegistryObject<Effect> WITHER_ATTACK_EFFECT = POTION_EFFECT.register("witherattackeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 4413801));

    public static final RegistryObject<Effect> GLUTTONY_TRIAL_EFFECT = POTION_EFFECT.register("gluttonytrialeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 3035801));
    public static final RegistryObject<Effect> ENVY_TRIAL_EFFECT = POTION_EFFECT.register("envytrialeffect", () -> new EnvyTrialEffect(EffectType.BENEFICIAL, 1012801));
    public static final RegistryObject<Effect> LUST_TRIAL_EFFECT = POTION_EFFECT.register("lusttrialeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 4413801));
    public static final RegistryObject<Effect> GREED_TRIAL_EFFECT = POTION_EFFECT.register("greedtrialeffect", () -> new PotionEffectBase(EffectType.BENEFICIAL, 2513801));
    public static final RegistryObject<Effect> SLOTH_TRIAL_EFFECT = POTION_EFFECT.register("slothtrialeffect", () -> new SlothTrialEffect(EffectType.BENEFICIAL, 4413801));
    public static final RegistryObject<Effect> WRATH_TRIAL_EFFECT = POTION_EFFECT.register("wrathtrialeffect", () -> new WrathTrialEffect(EffectType.BENEFICIAL, 1112801));
    public static final RegistryObject<Effect> PRIDE_TRIAL_EFFECT = POTION_EFFECT.register("pridetrialeffect", () -> new PrideTrialEffect(EffectType.BENEFICIAL, 3442801));

    public static final RegistryObject<Potion> GLUTTONY_TRIAL_POTION = POTION.register("gluttonytrialpotion", () -> new Potion("gluttonytrialpotion", new EffectInstance(GLUTTONY_TRIAL_EFFECT.get(), 30090)));
    public static final RegistryObject<Potion> ENVY_TRIAL_POTION = POTION.register("envytrialpotion", () -> new Potion("envytrialpotion", new EffectInstance(ENVY_TRIAL_EFFECT.get(), 30090)));
    public static final RegistryObject<Potion> LUST_TRIAL_POTION = POTION.register("lusttrialpotion", () -> new Potion("lusttrialpotion", new EffectInstance(LUST_TRIAL_EFFECT.get(), 30090)));
    public static final RegistryObject<Potion> GREED_TRIAL_POTION = POTION.register("greedtrialpotion", () -> new Potion("greedtrialpotion", new EffectInstance(GREED_TRIAL_EFFECT.get(), 30090)));
    public static final RegistryObject<Potion> SLOTH_TRIAL_POTION = POTION.register("slothtrialpotion", () -> new Potion("slothtrialpotion", new EffectInstance(SLOTH_TRIAL_EFFECT.get(), 30090)));
    public static final RegistryObject<Potion> WRATH_TRIAL_POTION = POTION.register("wrathtrialpotion", () -> new Potion("wrathtrialpotion", new EffectInstance(WRATH_TRIAL_EFFECT.get(), 30090)));
    public static final RegistryObject<Potion> PRIDE_TRIAL_POTION = POTION.register("pridetrialpotion", () -> new Potion("pridetrialpotion", new EffectInstance(PRIDE_TRIAL_EFFECT.get(), 30090)));

    public static void init(IEventBus _iEventBus)
    {
        POTION_EFFECT.register(_iEventBus);
        POTION.register(_iEventBus);
    }
}
