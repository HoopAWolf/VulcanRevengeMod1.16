package com.hoopawolf.vrm.client.gui;

import com.hoopawolf.vrm.config.ConfigHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

public class SinArmorFulfilmentGui extends AbstractGui
{
    public void draw(Minecraft mc, String text, MatrixStack matrixStack, String color)
    {
        int width = (int) ((float) mc.getMainWindow().getScaledWidth() + mc.getMainWindow().getGuiScaleFactor());
        int height = (int) ((float) mc.getMainWindow().getScaledHeight() + mc.getMainWindow().getGuiScaleFactor());

        RenderSystem.pushMatrix();
        RenderSystem.scalef(0.5F, 0.5F, 0.5F);
        drawCenteredString(matrixStack, mc.fontRenderer, text, (width / 2) + ConfigHandler.CLIENT.sinMaskWarningWidthOffset.get(), (height / 2) + ConfigHandler.CLIENT.sinMaskWarningHeightOffset.get(), Integer.parseInt(color, 16));
        RenderSystem.popMatrix();
    }
}
