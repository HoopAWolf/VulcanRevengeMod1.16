package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.structure.SwordStructure;
import com.hoopawolf.dmm.structure.piece.SwordStructurePiece;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureRegistryHandler
{
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Reference.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> SWORD_STONE_STRUCTURE = STRUCTURES.register("swordstructure", () -> new SwordStructure(NoFeatureConfig.field_236558_a_));

    public static IStructurePieceType SWORD_STRUCTURE_FEATURE = registerStructurePiece(SwordStructurePiece.Piece::new, SwordStructurePiece.SWORD_STRUCTURE_LOC);

    public static void generateStructureWorldSpawn()
    {
        registerStructureWorldSpawn(SWORD_STONE_STRUCTURE.get().func_236391_a_(NoFeatureConfig.field_236559_b_),
                new Biome[]{Biomes.FOREST, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_FOREST, Biomes.JUNGLE, Biomes.DARK_FOREST, Biomes.FLOWER_FOREST, Biomes.WOODED_HILLS, Biomes.TAIGA, Biomes.PLAINS});
    }

    protected static void registerStructureWorldSpawn(StructureFeature<?, ?> structureIn, Biome[] biomes)
    {
        for (Biome biome : biomes)
        {
            biome.func_235063_a_(structureIn);
        }
    }

    public static void init(IEventBus _iEventBus)
    {
        STRUCTURES.register(_iEventBus);
    }

    public static <C extends IFeatureConfig> IStructurePieceType registerStructurePiece(IStructurePieceType pieceType, ResourceLocation key)
    {
        return Registry.register(Registry.STRUCTURE_PIECE, key, pieceType);
    }
}
