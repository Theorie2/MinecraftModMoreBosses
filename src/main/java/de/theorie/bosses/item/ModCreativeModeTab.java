package de.theorie.bosses.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab BOSSES_TAB = new CreativeModeTab("bosses_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BLOODBOTTLE.get());
        }
    };
}
