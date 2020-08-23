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
        }
    }
}