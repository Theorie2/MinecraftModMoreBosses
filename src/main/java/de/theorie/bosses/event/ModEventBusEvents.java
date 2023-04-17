package de.theorie.bosses.event;

import de.theorie.bosses.MoreBosses;

import de.theorie.bosses.entity.ModEntityTypes;
import de.theorie.bosses.entity.custom.VampireEntity;
import de.theorie.bosses.recipe.RapierUpgradeStationRecipe;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MoreBosses.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerRecipeTypes(final RegisterEvent event){
        event.register(ForgeRegistries.Keys.RECIPE_TYPES,
                helper->helper.register(RapierUpgradeStationRecipe.Type.ID,RapierUpgradeStationRecipe.Type.INSTANCE)
    );
    }

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.VAMPIRE.get(), VampireEntity.setAttributes());
    }
}
