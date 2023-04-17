package de.theorie.bosses.recipe;

import de.theorie.bosses.MoreBosses;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreBosses.MODID);

public static final RegistryObject<RecipeSerializer<RapierUpgradeStationRecipe>> RAPIER_UPGRADE_SERIALIZER =
        SERIALIZERS.register("rapier_upgrading",()->RapierUpgradeStationRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
