package com.hoopawolf.vrm.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHandler
{
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static
    {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static
    {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Client
    {
        public final ForgeConfigSpec.IntValue sinMaskWarningHeightOffset;
        public final ForgeConfigSpec.IntValue sinMaskWarningWidthOffset;
        public final ForgeConfigSpec.BooleanValue sinMaskWarning;

        public Client(ForgeConfigSpec.Builder builder)
        {
            sinMaskWarningHeightOffset = builder
                    .translation("config.text.screenheight")
                    .defineInRange("sinmask.warn.height", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);

            sinMaskWarningWidthOffset = builder
                    .translation("config.text.screenwidth")
                    .defineInRange("sinmask.warn.width", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);

            sinMaskWarning = builder
                    .translation("config.text.showwarning")
                    .define("sinmask.warn.show", true);
        }
    }

    public static class Common
    {
        public final ForgeConfigSpec.IntValue minStructureAway;
        public final ForgeConfigSpec.IntValue maxStructureAway;
        public final ForgeConfigSpec.IntValue structureSpawnChance;
        public final ForgeConfigSpec.IntValue minSinStructureAway;
        public final ForgeConfigSpec.IntValue maxSinStructureAway;
        public final ForgeConfigSpec.IntValue structureSinSpawnChance;

        public final ForgeConfigSpec.BooleanValue slothMaskTurnNight;
        public final ForgeConfigSpec.BooleanValue slothMaskTurnDay;
        public final ForgeConfigSpec.BooleanValue greedDoubleDrop;

        public final ForgeConfigSpec.ConfigValue<String> pesBowItem;
        public final ForgeConfigSpec.ConfigValue<String> warSwordItem;
        public final ForgeConfigSpec.ConfigValue<String> deathScytheItem;
        public final ForgeConfigSpec.ConfigValue<String> famineScaleItem;

        public final ForgeConfigSpec.ConfigValue<String> gluttonyItemOne;
        public final ForgeConfigSpec.ConfigValue<String> gluttonyItemTwo;
        public final ForgeConfigSpec.ConfigValue<String> gluttonyItemThree;

        public final ForgeConfigSpec.ConfigValue<String> envyItemOne;
        public final ForgeConfigSpec.ConfigValue<String> envyItemTwo;
        public final ForgeConfigSpec.ConfigValue<String> envyItemThree;

        public final ForgeConfigSpec.ConfigValue<String> lustItemOne;
        public final ForgeConfigSpec.ConfigValue<String> lustItemTwo;
        public final ForgeConfigSpec.ConfigValue<String> lustItemThree;

        public final ForgeConfigSpec.ConfigValue<String> greedItemOne;
        public final ForgeConfigSpec.ConfigValue<String> greedItemTwo;
        public final ForgeConfigSpec.ConfigValue<String> greedItemThree;

        public final ForgeConfigSpec.ConfigValue<String> slothItemOne;
        public final ForgeConfigSpec.ConfigValue<String> slothItemTwo;
        public final ForgeConfigSpec.ConfigValue<String> slothItemThree;

        public final ForgeConfigSpec.ConfigValue<String> wrathItemOne;
        public final ForgeConfigSpec.ConfigValue<String> wrathItemTwo;
        public final ForgeConfigSpec.ConfigValue<String> wrathItemThree;

        public final ForgeConfigSpec.ConfigValue<String> prideItemOne;
        public final ForgeConfigSpec.ConfigValue<String> prideItemTwo;
        public final ForgeConfigSpec.ConfigValue<String> prideItemThree;

        public Common(ForgeConfigSpec.Builder builder)
        {
            minStructureAway = builder
                    .translation("config.text.minstruct")
                    .defineInRange("structure.spawndist.min", 7, 0, Integer.MAX_VALUE);

            maxStructureAway = builder
                    .translation("config.text.maxstruct")
                    .defineInRange("structure.spawndist.max", 12, 0, Integer.MAX_VALUE);

            structureSpawnChance = builder
                    .translation("config.text.structperc")
                    .defineInRange("structure.spawnpercentage", 40, 0, 100);

            minSinStructureAway = builder
                    .translation("config.text.minsinstruct")
                    .defineInRange("sinstructure.spawndist.min", 7, 0, Integer.MAX_VALUE);

            maxSinStructureAway = builder
                    .translation("config.text.maxsinstruct")
                    .defineInRange("sinstructure.spawndist.max", 12, 0, Integer.MAX_VALUE);

            structureSinSpawnChance = builder
                    .translation("config.text.sinstructperc")
                    .defineInRange("sinstructure.spawnpercentage", 40, 0, 100);

            slothMaskTurnNight = builder
                    .translation("config.text.slothmasksleep")
                    .define("sinmask.sloth.changeToNight", true);

            slothMaskTurnDay = builder
                    .translation("config.text.slothmasksleepday")
                    .define("sinmask.sloth.changeToDay", true);

            greedDoubleDrop = builder
                    .translation("config.text.greedmaskdouble")
                    .define("sinmask.greed.allowDoubleDrop", true);

            warSwordItem = builder
                    .translation("config.text.warsworditem")
                    .define("fourhorseman.war.item", "item.minecraft.diamond_sword");

            pesBowItem = builder
                    .translation("config.text.pesbowitem")
                    .define("fourhorseman.pestilence.item", "item.minecraft.bow");

            deathScytheItem = builder
                    .translation("config.text.deathscytheitem")
                    .define("fourhorseman.death.item", "item.minecraft.diamond_hoe");

            famineScaleItem = builder
                    .translation("config.text.faminescaleitem")
                    .define("fourhorseman.famine.item", "item.minecraft.diamond_pickaxe");

            gluttonyItemOne = builder
                    .translation("config.text.gluttonyitemone")
                    .define("sevendeadlysins.gluttony.itemone", "item.minecraft.cooked_beef");

            gluttonyItemTwo = builder
                    .translation("config.text.gluttonyitemtwo")
                    .define("sevendeadlysins.gluttony.itemtwo", "item.minecraft.cooked_cod");

            gluttonyItemThree = builder
                    .translation("config.text.gluttonyitemthree")
                    .define("sevendeadlysins.gluttony.itemthree", "item.minecraft.cooked_rabbit");

            envyItemOne = builder
                    .translation("config.text.envyitemone")
                    .define("sevendeadlysins.envy.itemone", "item.minecraft.slime_ball");

            envyItemTwo = builder
                    .translation("config.text.envyitemtwo")
                    .define("sevendeadlysins.envy.itemtwo", "item.minecraft.magma_cream");

            envyItemThree = builder
                    .translation("config.text.envyitemthree")
                    .define("sevendeadlysins.envy.itemthree", "block.minecraft.slime_block");

            lustItemOne = builder
                    .translation("config.text.lustitemone")
                    .define("sevendeadlysins.lust.itemone", "item.minecraft.wheat");

            lustItemTwo = builder
                    .translation("config.text.lustitemtwo")
                    .define("sevendeadlysins.lust.itemtwo", "item.minecraft.wheat_seeds");

            lustItemThree = builder
                    .translation("config.text.lustitemthree")
                    .define("sevendeadlysins.lust.itemthree", "item.minecraft.carrot");

            greedItemOne = builder
                    .translation("config.text.greeditemone")
                    .define("sevendeadlysins.greed.itemone", "item.minecraft.diamond");

            greedItemTwo = builder
                    .translation("config.text.greeditemtwo")
                    .define("sevendeadlysins.greed.itemtwo", "item.minecraft.gold_ingot");

            greedItemThree = builder
                    .translation("config.text.greeditemthree")
                    .define("sevendeadlysins.greed.itemthree", "item.minecraft.emerald");

            slothItemOne = builder
                    .translation("config.text.slothitemone")
                    .define("sevendeadlysins.sloth.itemone", "block.minecraft.white_bed");

            slothItemTwo = builder
                    .translation("config.text.slothitemtwo")
                    .define("sevendeadlysins.sloth.itemtwo", "block.minecraft.brown_bed");

            slothItemThree = builder
                    .translation("config.text.slothitemthree")
                    .define("sevendeadlysins.sloth.itemthree", "block.minecraft.lime_bed");

            wrathItemOne = builder
                    .translation("config.text.wrathitemone")
                    .define("sevendeadlysins.wrath.itemone", "item.minecraft.diamond_sword");

            wrathItemTwo = builder
                    .translation("config.text.wrathitemtwo")
                    .define("sevendeadlysins.wrath.itemtwo", "item.minecraft.shield");

            wrathItemThree = builder
                    .translation("config.text.wrathitemthree")
                    .define("sevendeadlysins.wrath.itemthree", "item.minecraft.diamond_chestplate");

            prideItemOne = builder
                    .translation("config.text.prideitemone")
                    .define("sevendeadlysins.pride.itemone", "item.minecraft.bow");

            prideItemTwo = builder
                    .translation("config.text.prideitemtwo")
                    .define("sevendeadlysins.pride.itemtwo", "item.minecraft.golden_apple");

            prideItemThree = builder
                    .translation("config.text.prideitemthree")
                    .define("sevendeadlysins.pride.itemthree", "item.minecraft.ender_eye");
        }
    }
}