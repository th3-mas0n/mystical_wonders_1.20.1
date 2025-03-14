package net.chryseon.mystical_wonders.block.entity.render;

import net.chryseon.mystical_wonders.block.entity.BellEntity;
import net.chryseon.mystical_wonders.block.entity.render.model.BellModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.awt.*;

public class BellRender extends GeoBlockRenderer<BellEntity> {
    GeoModel<BellEntity> stone = new BellModel();

    public BellRender() {
        super(new BellModel());
    }

    public void render(BellEntity tile, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        // Render the marks model
        BakedGeoModel modelMarks = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(tile));
        stack.push();
        stack.translate(0, 0.01f, 0);
        stack.translate(0.5, 0, 0.5);

        Identifier textureResource = getTextureResource(tile);
        RenderLayer marksRenderType = getRenderType(tile, partialTicks, stack, bufferIn, null, packedLightIn, textureResource);
        VertexConsumer vertexConsumer = bufferIn.getBuffer(marksRenderType);

        actuallyRender(
                stack, tile, modelMarks, marksRenderType, bufferIn, vertexConsumer,
                false, partialTicks, packedLightIn, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f
        );

        stack.pop();

        // Render the stone model
        BakedGeoModel model = stone.getBakedModel(stone.getModelResource(tile));
        stack.push();
        stack.translate(0, 0.01f, 0);
        stack.translate(0.5, 0, 0.5);

        Color renderColor = getRenderColor(tile, partialTicks, packedLightIn); // Updated method call
        RenderLayer renderType = getRenderType(tile, partialTicks, stack, bufferIn, null, packedLightIn, textureResource);
        vertexConsumer = bufferIn.getBuffer(renderType);

        actuallyRender(
                stack, tile, model, renderType, bufferIn, vertexConsumer,
                false, partialTicks, packedLightIn, OverlayTexture.DEFAULT_UV,
                (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
                (float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255f
        );

        stack.pop();
    }

    public Identifier getTextureResource(BellEntity instance) {
        return new BellModel().getTextureResource(instance);
    }

    public RenderLayer getRenderType(BellEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }
}