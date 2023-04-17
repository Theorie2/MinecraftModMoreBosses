package de.theorie.bosses.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier SILVER = new ForgeTier(2,1400, 1.5f,5,10, BlockTags.NEEDS_IRON_TOOL,
            ()-> Ingredient.of(ModItems.SILVER_INGOT.get()));
    public static final ForgeTier WOOD = new ForgeTier(1,80, 2f,4,6,BlockTags.LOGS,
            ()-> Ingredient.of(Items.OAK_LOG));


}
