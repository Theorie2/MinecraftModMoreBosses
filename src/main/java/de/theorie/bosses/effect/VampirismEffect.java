package de.theorie.bosses.effect;

import de.theorie.bosses.MoreBosses;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class VampirismEffect extends MobEffect{
    private int duration;
    protected VampirismEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide()){
           if(duration<=10){
               pLivingEntity.kill();

           }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }}

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        duration=pDuration;
        return true;
    }
}
