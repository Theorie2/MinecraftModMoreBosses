package de.theorie.bosses.entity;

import de.theorie.bosses.MoreBosses;
import de.theorie.bosses.entity.custom.BadEntity;
import de.theorie.bosses.entity.custom.VampireEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MoreBosses.MODID);

    public static final RegistryObject<EntityType<VampireEntity>> VAMPIRE = ENTITY_TYPES.register(
            "vampire",
            () -> EntityType.Builder.of(VampireEntity::new, MobCategory.MONSTER)
                    .sized(0.5f,2f)
                    .build(new ResourceLocation(MoreBosses.MODID,"vampire").toString()));

    public static final RegistryObject<EntityType<BadEntity>> BAD = ENTITY_TYPES.register(
            "bad",
            () -> EntityType.Builder.of(BadEntity::new, MobCategory.MONSTER)
                    .sized(0.5f,0.5f)
                    .build(new ResourceLocation(MoreBosses.MODID,"bad").toString()));




    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
