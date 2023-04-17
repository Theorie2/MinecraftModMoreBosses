package de.theorie.bosses.item.custom;

import de.theorie.bosses.entity.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RapierItemLvlTwo extends SwordItem {

    public RapierItemLvlTwo(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level.isClientSide) {
                pTarget.hurt(DamageSource.MAGIC,5);
        }
        if (!pAttacker.level.isClientSide&&pTarget.getType() == ModEntityTypes.VAMPIRE.get()) {
            pTarget.hurt(DamageSource.MAGIC,1000);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            pPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
            pPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30, 0));
        }

        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }



}
