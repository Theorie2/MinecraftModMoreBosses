package de.theorie.bosses.entity.client;

import de.theorie.bosses.MoreBosses;
import de.theorie.bosses.entity.custom.BadEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BadModel extends AnimatedGeoModel<BadEntity> {
    @Override
    public ResourceLocation getModelResource(BadEntity object) {
        return new ResourceLocation(MoreBosses.MODID,"geo/bad.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BadEntity object) {
        return new ResourceLocation(MoreBosses.MODID,"textures/entity/bad/bad.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BadEntity animatable) {
        return new ResourceLocation(MoreBosses.MODID,"animations/bad.animation.json");
    }
}
