package net.salju.kobolds;

import net.salju.kobolds.init.*;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod("kobolds")
public class Kobolds {
	public static final String MODID = "kobolds";

	public Kobolds(IEventBus bus) {
		KoboldsSounds.REGISTRY.register(bus);
		KoboldsItems.REGISTRY.register(bus);
		KoboldsTabs.REGISTRY.register(bus);
		KoboldsMobs.REGISTRY.register(bus);
		KoboldsPotions.REGISTRY.register(bus);
		KoboldsStructures.REGISTRY.register(bus);
	}
}