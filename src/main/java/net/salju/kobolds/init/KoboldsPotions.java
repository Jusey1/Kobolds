package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class KoboldsPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, Kobolds.MODID);
	public static final DeferredHolder<Potion, Potion> AMETHYST = REGISTRY.register("amethyst", () -> new Potion("amethyst", new MobEffectInstance(MobEffects.INSTANT_HEALTH, 0, 0, false, true), new MobEffectInstance(MobEffects.REGENERATION, 1200, 0, false, true)));
}