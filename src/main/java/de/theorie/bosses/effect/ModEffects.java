package de.theorie.bosses.effect;

import de.theorie.bosses.MoreBosses;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MoreBosses.MODID);

    public static final RegistryObject<MobEffect> VAMPIRISM = MOB_EFFECTS.register("vampirism_effect",
            ()-> new VampirismEffect(MobEffectCategory.NEUTRAL,8126464));



    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
