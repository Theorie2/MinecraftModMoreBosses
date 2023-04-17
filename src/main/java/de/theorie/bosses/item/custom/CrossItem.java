package de.theorie.bosses.item.custom;


import de.theorie.bosses.MoreBosses;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;



public class CrossItem extends Item{
    public CrossItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
            if(!pContext.getLevel().isClientSide()){
                BlockPos blockPos = pContext.getClickedPos();
                Player player = pContext.getPlayer();
                Level level = pContext.getLevel();




            pContext.getPlayer().addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 5));
            player.addEffect(new MobEffectInstance(MobEffects.HEAL,1,5));

        if(isHunterStructureOne(pContext,blockPos)){
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,600,1));
        }}

        pContext.getItemInHand().hurtAndBreak(1,pContext.getPlayer(), (player)->player.broadcastBreakEvent(player.getUsedItemHand()));
        return super.useOn(pContext);
}


public boolean isHunterStructureOne(UseOnContext pContext,BlockPos blockPos) {
    if (pContext.getLevel().getBlockState(blockPos).getBlock() == Blocks.CANDLE) {
        if (pContext.getLevel().getBlockState(blockPos).getBlock().getLightEmission(pContext.getLevel().getBlockState(blockPos),
                null,blockPos)>=1) {
            return true;
        }
}
        return false;
}
}


