package com.hoopawolf.vrm.structure;

import com.hoopawolf.vrm.config.ConfigHandler;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.structure.piece.SwordStructurePiece;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class SwordStructure extends Structure<NoFeatureConfig>
{
    public SwordStructure(Codec<NoFeatureConfig> p_i231997_1_)
    {
        super(p_i231997_1_);
    }

    @Override
    public String getStructureName()
    {
        return SwordStructurePiece.SWORD_STRUCTURE_LOC.toString();
    }

    protected ChunkPos getStartPositionForPosition(ChunkGenerator chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ)
    {
        int featureDistance = ConfigHandler.COMMON.maxStructureAway.get();
        int featureSeparation = ConfigHandler.COMMON.minStructureAway.get();

        int xTemp = x + featureDistance * spacingOffsetsX;
        int zTemp = z + featureDistance * spacingOffsetsZ;
        int validChunkX = (xTemp < 0 ? xTemp - featureDistance + 1 : xTemp) / featureDistance;
        int validChunkZ = (zTemp < 0 ? zTemp - featureDistance + 1 : zTemp) / featureDistance;
        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(62353535, x, z, 62226333);
        validChunkX *= featureDistance;
        validChunkZ *= featureDistance;
        validChunkX += random.nextInt(featureDistance - featureSeparation) + random.nextInt(featureDistance - featureSeparation) / 2;
        validChunkZ += random.nextInt(featureDistance - featureSeparation) + random.nextInt(featureDistance - featureSeparation) / 2;
        return new ChunkPos(validChunkX, validChunkZ);
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator p_230363_1_, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome p_230363_8_, ChunkPos p_230363_9_, NoFeatureConfig p_230363_10_)
    {
        ChunkPos chunkpos = this.getStartPositionForPosition(p_230363_1_, p_230363_5_, p_230363_6_, p_230363_7_, 0, 0);

        if (p_230363_6_ == chunkpos.x && p_230363_7_ == chunkpos.z && p_230363_5_.nextInt(100) < ConfigHandler.COMMON.structureSpawnChance.get())
        {
            return p_230363_2_.hasStructure(this);
        }

        return false;
    }

    @Override
    public Structure.IStartFactory<NoFeatureConfig> getStartFactory()
    {
        return SwordStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration func_236396_f_()
    {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends StructureStart<NoFeatureConfig>
    {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn)
        {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230366_a_(ISeedReader p_230366_1_, StructureManager p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, MutableBoundingBox p_230366_5_, ChunkPos p_230366_6_)
        {
            super.func_230366_a_(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
            int i = this.bounds.minY;

            for (int j = p_230366_5_.minX; j <= p_230366_5_.maxX; ++j)
            {
                for (int k = p_230366_5_.minZ; k <= p_230366_5_.maxZ; ++k)
                {
                    BlockPos blockpos = new BlockPos(j, i, k);
                    if (!p_230366_1_.isAirBlock(blockpos) && this.bounds.isVecInside(blockpos))
                    {
                        boolean flag = false;

                        for (StructurePiece structurepiece : this.components)
                        {
                            if (structurepiece.getBoundingBox().isVecInside(blockpos))
                            {
                                flag = true;
                                break;
                            }
                        }

                        if (flag)
                        {
                            for (int l = i - 1; l > 1; --l)
                            {
                                BlockPos blockpos1 = new BlockPos(j, l, k);
                                if (!p_230366_1_.isAirBlock(blockpos1) && !p_230366_1_.getBlockState(blockpos1).getMaterial().isLiquid())
                                {
                                    break;
                                }

                                if (p_230366_1_.getRandom().nextInt(100) > 10)
                                {
                                    if (p_230366_1_.getRandom().nextInt(100) < 30)
                                    {
                                        p_230366_1_.setBlockState(blockpos1, Blocks.STONE_BRICKS.getDefaultState(), 2);
                                    } else if (p_230366_1_.getRandom().nextInt(100) > 70)
                                    {
                                        p_230366_1_.setBlockState(blockpos1, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 2);
                                    } else
                                    {
                                        p_230366_1_.setBlockState(blockpos1, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 2);
                                    }
                                } else
                                {
                                    p_230366_1_.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }

        }

        @Override
        public void func_230364_a_(ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig p_230364_6_)
        {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            int x = (chunkX << 4);
            int z = (chunkZ << 4);

            int surfaceY = generator.getNoiseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG) - 1;
            BlockPos blockpos = new BlockPos(x, surfaceY, z);

            SwordStructurePiece.start(templateManagerIn, blockpos, rotation, this.components, this.rand);

            this.recalculateStructureSize();

            Reference.LOGGER.log(Level.DEBUG, "Sword Structure at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
        }
    }
}