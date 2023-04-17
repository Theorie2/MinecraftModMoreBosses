package de.theorie.bosses.entity.client;

import de.theorie.bosses.MoreBosses;
import de.theorie.bosses.entity.custom.VampireEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class VampireModel extends AnimatedGeoModel<VampireEntity> {
    @Override
    public ResourceLocation getModelResource(VampireEntity object) {
        return new ResourceLocation(MoreBosses.MODID,"geo/vampire.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VampireEntity object) {
        return new ResourceLocation(MoreBosses.MODID,"textures/entity/vampire/vampire.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VampireEntity animatable) {
        return new ResourceLocation(MoreBosses.MODID,"animations/vampire.animation.json");
    }
}
