package de.theorie.bosses.item.custom;

import de.theorie.bosses.effect.ModEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VampireToothItem extends Item {
    public VampireToothItem(Properties pProperties) {
        super(pProperties);
    }
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
    ItemStack itemstack = pPlayer.getItemInHand(pHand);

        if (!pLevel.isClientSide) {
            pPlayer.hurt(DamageSource.CACTUS,1);
            pPlayer.addEffect(new MobEffectInstance(ModEffects.VAMPIRISM.get(),4000, 0));
            itemstack.shrink(1);
        }
    return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());}
}
