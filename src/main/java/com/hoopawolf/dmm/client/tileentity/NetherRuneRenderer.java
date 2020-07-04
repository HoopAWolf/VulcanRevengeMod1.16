package com.hoopawolf.dmm.client.tileentity;

import com.hoopawolf.dmm.blocks.tileentity.NetherRuneTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherRuneRenderer extends RuneRenderer<NetherRuneTileEntity>
{
    public NetherRuneRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }
}