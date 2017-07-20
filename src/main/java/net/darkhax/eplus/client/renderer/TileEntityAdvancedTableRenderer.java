package net.darkhax.eplus.client.renderer;

import net.darkhax.eplus.tileentity.TileEntityAdvancedTable;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityAdvancedTableRenderer extends TileEntitySpecialRenderer<TileEntityAdvancedTable> {

    private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("eplus:textures/entity/enchantingplus_book.png");
    private final ModelBook modelBook = new ModelBook();

    @Override
    public void render (TileEntityAdvancedTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
        final float f = te.tickCount + partialTicks;
        GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
        float f1;

        for (f1 = te.bookRotation - te.bookRotationPrev; f1 >= (float) Math.PI; f1 -= (float) Math.PI * 2F) {
            ;
        }

        while (f1 < -(float) Math.PI) {
            f1 += (float) Math.PI * 2F;
        }

        final float f2 = te.bookRotationPrev + f1 * partialTicks;
        GlStateManager.rotate(-f2 * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
        this.bindTexture(TEXTURE_BOOK);
        float f3 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.25F;
        float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.75F;
        f3 = (f3 - MathHelper.ceil(f3)) * 1.6F - 0.3F;
        f4 = (f4 - MathHelper.ceil(f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        final float f5 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
        GlStateManager.enableCull();
        this.modelBook.render((Entity) null, f, f3, f4, f5, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
