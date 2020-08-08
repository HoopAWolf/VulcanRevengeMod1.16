package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.structure.SwordStructure;
import com.hoopawolf.vrm.structure.piece.SwordStructurePiece;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
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

    public static final IStructurePieceType SWORD_STRUCTURE_FEATURE = registerStructurePiece(SwordStructurePiece.Piece::new, SwordStructurePiece.SWORD_STRUCTURE_LOC);

    public static void generateStructureWorldSpawn()
    {
        Structure.field_236365_a_.put(SWORD_STONE_STRUCTURE.get().getStructureName(), SWORD_STONE_STRUCTURE.get());
        registerStructureWorldSpawn(SWORD_STONE_STRUCTURE.get().func_236391_a_(NoFeatureConfig.field_236559_b_), Biome.TempCategory.MEDIUM);
    }

    protected static void registerStructureWorldSpawn(StructureFeature<?, ?> structureIn, Biome.TempCategory temp)
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            if (biome.getTempCategory().equals(temp))
            {
                biome.func_235063_a_(structureIn);
            }
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
