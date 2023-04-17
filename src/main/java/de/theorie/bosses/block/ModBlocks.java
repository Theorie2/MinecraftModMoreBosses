package de.theorie.bosses.block;

import de.theorie.bosses.MoreBosses;
import de.theorie.bosses.block.custom.RapierUpgradeStationBlock;
import de.theorie.bosses.block.custom.SunBlock;
import de.theorie.bosses.item.ModCreativeModeTab;
import de.theorie.bosses.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreBosses.MODID);

        public static final RegistryObject<Block> BLOODY_DIRT = registerBlock("bloody_dirt",
                ()->new Block(BlockBehaviour.Properties.of(Material.DIRT)
                        .strength(0.2F,2.0F).sound(SoundType.MUD)),ModCreativeModeTab.BOSSES_TAB);

        public static final RegistryObject<Block> SILVER_BLOCK = registerBlock("silver_block",
                ()->new Block(BlockBehaviour.Properties.of(Material.METAL)
                        .strength(7.0F,5.0F).requiresCorrectToolForDrops().sound(SoundType.METAL)), ModCreativeModeTab.BOSSES_TAB);

       public static final RegistryObject<Block> SILVER_ORE = registerBlock("silver_ore",
                ()->new Block(BlockBehaviour.Properties.of(Material.STONE)
                        .strength(3.0F,3.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)),ModCreativeModeTab.BOSSES_TAB);
        public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
                ()->new Block(BlockBehaviour.Properties.of(Material.STONE)
                        .strength(4.0F,3.0F).requiresCorrectToolForDrops()),ModCreativeModeTab.BOSSES_TAB);

        public static final RegistryObject<Block> SUNBLOCK = registerBlock("sun_block",
            ()->new SunBlock(BlockBehaviour.Properties.of(Material.FROGLIGHT).instabreak()),ModCreativeModeTab.BOSSES_TAB);

        public static final RegistryObject<Block> RAPIER_UPGRADE_STATION = registerBlock("rapier_upgrade_station",
            ()->new RapierUpgradeStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()),
                ModCreativeModeTab.BOSSES_TAB);




    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block,CreativeModeTab tab ){
                RegistryObject<T> toReturn = BLOCKS.register(name, block);
                registerBlockItem(name, toReturn,tab);
                return  toReturn;
        }
        private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
         return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                 new Item.Properties().tab(tab)));
        }

        public static void register(IEventBus eventBus){
                BLOCKS.register(eventBus);
        }
}
