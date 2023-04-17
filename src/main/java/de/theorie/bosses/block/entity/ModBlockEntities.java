package de.theorie.bosses.block.entity;

import de.theorie.bosses.MoreBosses;
import de.theorie.bosses.block.ModBlocks;
import de.theorie.bosses.block.entity.custom.RapierUpgradeStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MoreBosses.MODID);

    public static final RegistryObject<BlockEntityType<RapierUpgradeStationBlockEntity>> RAPIER_UPGRADE_STATION_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("rapier_upgrade_block_entity", () ->
                    BlockEntityType.Builder.of(RapierUpgradeStationBlockEntity::new,
                            ModBlocks.RAPIER_UPGRADE_STATION.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
