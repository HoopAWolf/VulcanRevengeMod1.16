package com.hoopawolf.vrm.client.renderer;

import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PesArrowRenderer extends ArrowRenderer<PesArrowEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/pesarrow.png");

    public PesArrowRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    protected int func_239381_b_(PesArrowEntity entityIn, BlockPos blockPos)
    {
        return 15;
    }

    @Override
    public ResourceLocation getEntityTexture(PesArrowEntity _entity)
    {
        return TEXTURE;
    }

}
