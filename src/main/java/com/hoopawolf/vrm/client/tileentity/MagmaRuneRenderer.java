package com.hoopawolf.vrm.client.tileentity;

import com.hoopawolf.vrm.blocks.tileentity.MagmaRuneTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MagmaRuneRenderer extends RuneRenderer<MagmaRuneTileEntity>
{
    public MagmaRuneRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }
}