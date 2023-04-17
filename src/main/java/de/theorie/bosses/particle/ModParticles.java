package de.theorie.bosses.particle;

import de.theorie.bosses.MoreBosses;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MoreBosses.MODID);

    //public  static final RegistryObject<SimpleParticleType> IRON_SPARKS_PARTICLE =
     //       PARTICLE_TYPES.register("iron_sparks_particles", () -> new SimpleParticleType(true));
    public static void register(IEventBus eventBus){
    PARTICLE_TYPES.register(eventBus);
    }
}
