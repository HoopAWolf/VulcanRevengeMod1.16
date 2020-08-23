package com.hoopawolf.vrm.client.tileentity;

import com.hoopawolf.vrm.blocks.tileentity.SwordStoneTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class SwordStoneRenderer extends TileEntityRenderer<SwordStoneTileEntity>
{
    private static final float field_229057_l_ = (float) (Math.sqrt(3.0D) / 2.0D);

    public SwordStoneRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    private static void func_229061_a_(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_)
    {
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void func_229060_a_(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_, Vector3i color)
    {
        p_229060_0_.pos(p_229060_1_, -field_229057_l_ * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(color.getX(), color.getY(), color.getZ(), 0).endVertex();
    }

    private static void func_229062_b_(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_, Vector3i color)
    {
        p_229062_0_.pos(p_229062_1_, field_229057_l_ * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(color.getX(), color.getY(), color.getZ(), 0).endVertex();
    }

    private static void func_229063_c_(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_, Vector3i color)
    {
        p_229063_0_.pos(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(color.getX(), color.getY(), color.getZ(), 0).endVertex();
    }

    @Override
    public void render(SwordStoneTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        Random random = new Random();

        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 0.55D, 0.5D);
        if (tileEntityIn.getDegree() <= 0 && tileEntityIn.isActivated())
        {
            matrixStackIn.push();
            matrixStackIn.translate(random.nextGaussian() * 0.005D, 0.0D, random.nextGaussian() * 0.005D);
        }
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        matrixStackIn.translate(0D, (tileEntityIn.getTimer() * 0.0125F) * 0.2F, 0D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.getDegree()));
        matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(-135));
        renderItem(tileEntityIn.getActivationItem().getDefaultInstance(), partialTicks, matrixStackIn, bufferIn, combinedLightIn);

        if (tileEntityIn.getDegree() <= 0 && tileEntityIn.isActivated())
        {
            matrixStackIn.pop();
        }

        if (tileEntityIn.isDone())
        {
            float f5 = 0.08F;
            float f7 = 0.0F;

            IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.getLightning());
            matrixStackIn.push();
            matrixStackIn.scale(0.02F, 0.02F, 0.02F);

            for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i)
            {
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(360.0F));
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
                float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
                int j = (int) (255.0F * (1.0F - f7));
                func_229061_a_(ivertexbuilder2, matrix4f, j);
                func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, new Vector3i(255, 255, 127));
                func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, new Vector3i(0, 0, 255));
                func_229061_a_(ivertexbuilder2, matrix4f, j);
                func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, new Vector3i(255, 255, 255));
                func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, new Vector3i(127, 127, 255));
                func_229061_a_(ivertexbuilder2, matrix4f, j);
                func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, new Vector3i(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, new Vector3i(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            }

            matrixStackIn.pop();
        }

        matrixStackIn.pop();
    }

    private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                            int combinedLightIn)
    {
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
    }
}