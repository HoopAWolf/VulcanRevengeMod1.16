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
                    .comment("Screen Height Offset for sin mask warning text. Use [<] & [>] to change in game")
                    .defineInRange("sinmask.warn.height", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);

            sinMaskWarningWidthOffset = builder
                    .comment("Screen Width Offset for sin mask warning text. Use [;] & ['] to change in game")
                    .defineInRange("sinmask.warn.width", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);

            sinMaskWarning = builder
                    .comment("Show warning for Sin mask")
                    .define("sinmask.warn.show", true);
        }
    }

    public static class Common
    {
        public final ForgeConfigSpec.IntValue minStructureAway;
        public final ForgeConfigSpec.IntValue maxStructureAway;
        public final ForgeConfigSpec.IntValue structureSpawnChance;

        public final ForgeConfigSpec.BooleanValue slothMaskTurnNight;
        public final ForgeConfigSpec.BooleanValue slothMaskTurnDay;

        public Common(ForgeConfigSpec.Builder builder)
        {
            minStructureAway = builder
                    .comment("Min distance away from chosen spot")
                    .defineInRange("structure.spawndist.min", 7, 0, Integer.MAX_VALUE);

            maxStructureAway = builder
                    .comment("Max distance away from chosen spot")
                    .defineInRange("structure.spawndist.max", 12, 0, Integer.MAX_VALUE);

            structureSpawnChance = builder
                    .comment("Structure spawning percentage")
                    .defineInRange("structure.spawnpercentage", 40, 0, 100);

            slothMaskTurnNight = builder
                    .comment("Sloth Mask allow setting to night when sleep in day")
                    .define("sinmask.sloth.changeToNight", true);

            slothMaskTurnDay = builder
                    .comment("Sloth Mask allow setting to day when sleep in night")
                    .define("sinmask.sloth.changeToDay", true);
        }
    }
}