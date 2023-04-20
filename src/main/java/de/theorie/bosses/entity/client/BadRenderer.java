package de.theorie.bosses.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.theorie.bosses.MoreBosses;
import de.theorie.bosses.entity.custom.BadEntity;
import de.theorie.bosses.entity.custom.BadEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BadRenderer extends GeoEntityRenderer<BadEntity> {
    public BadRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BadModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(BadEntity animatable) {
        return new ResourceLocation(MoreBosses.MODID,"textures/entity/bad/bad.png");
    }

    @Override
    public RenderType getRenderType(BadEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.4F, 0.4F, 0.4F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
