package de.theorie.bosses.item;

import de.theorie.bosses.MoreBosses;
import de.theorie.bosses.item.custom.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {


    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MoreBosses.MODID);

    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB).food(ModFoods.GARLIC)));
    public static final RegistryObject<Item> BLOODBOTTLE = ITEMS.register("blood_bottle",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB).stacksTo(16)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB)));
    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB)));
    public static final RegistryObject<Item> CROSS = ITEMS.register("cross",
            () -> new CrossItem(new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB).stacksTo(1).durability(30)));
    public static final RegistryObject<Item> VAMPIRE_TOOTH = ITEMS.register("vampire_tooth",
            () -> new VampireToothItem(new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB)));


    public static final RegistryObject<Item> SILVER_RAPIER_LVL_ONE = ITEMS.register("silver_rapier_lvl_one",
            () -> new RapierItemLvlOne(ModTiers.SILVER,1,1,new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB).durability(1800)));
    public static final RegistryObject<Item> SILVER_RAPIER_LVL_TWO = ITEMS.register("silver_rapier_lvl_two",
            () -> new RapierItemLvlTwo(ModTiers.SILVER,1,1,new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB).durability(1800)));
    public static final RegistryObject<Item> POLE = ITEMS.register("pole",
            () -> new PoleItem(ModTiers.WOOD,1,1,new Item.Properties().tab(ModCreativeModeTab.BOSSES_TAB)));




    public static void register(IEventBus eventBus){
    ITEMS.register(eventBus);
    }
}
