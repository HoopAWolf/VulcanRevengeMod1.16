package com.hoopawolf.vrm.client.tileentity;

import com.hoopawolf.vrm.blocks.tileentity.ScuteRuneTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScuteRuneRenderer extends RuneRenderer<ScuteRuneTileEntity>
{
    public ScuteRuneRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }
}