package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;

public class KoboldsSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, Kobolds.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> KOBOLD_TRADE = REGISTRY.register("kobold_trade", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_trade")));
	public static final DeferredHolder<SoundEvent, SoundEvent> KOBOLD_PURR = REGISTRY.register("kobold_purr", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_purr")));
	public static final DeferredHolder<SoundEvent, SoundEvent> KOBOLD_DEATH = REGISTRY.register("kobold_death", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_death")));
	public static final DeferredHolder<SoundEvent, SoundEvent> KOBOLD_HURT = REGISTRY.register("kobold_hurt", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_hurt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> KOBOLD_IDLE = REGISTRY.register("kobold_idle", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_idle")));
	public static final DeferredHolder<SoundEvent, SoundEvent> KOBOLD_YIP = REGISTRY.register("kobold_yip", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_yip")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_KOBBLESTONE = REGISTRY.register("music_kobblestone", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Kobolds.MODID, "music_kobblestone")));
}