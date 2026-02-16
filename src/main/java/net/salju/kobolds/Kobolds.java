package net.salju.kobolds;

import net.salju.kobolds.init.*;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod("kobolds")
public class Kobolds {
	public static final String MODID = "kobolds";

	public Kobolds(IEventBus bus) {
		NeoForge.EVENT_BUS.register(this);
		KoboldsSounds.REGISTRY.register(bus);
		KoboldsBlocks.REGISTRY.register(bus);
		KoboldsItems.REGISTRY.register(bus);
		KoboldsTabs.REGISTRY.register(bus);
		KoboldsMobs.REGISTRY.register(bus);
		KoboldsPotions.REGISTRY.register(bus);
		KoboldsStructures.REGISTRY.register(bus);
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		//
	}
}