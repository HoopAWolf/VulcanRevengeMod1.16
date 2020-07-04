package com.hoopawolf.dmm.client.tileentity;

import com.hoopawolf.dmm.blocks.tileentity.BlazeRuneTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazeRuneRenderer extends RuneRenderer<BlazeRuneTileEntity>
{
    public BlazeRuneRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }
}