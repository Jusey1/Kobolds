package net.salju.kobolds;

import net.salju.kobolds.init.*;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.minecraft.util.Tuple;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

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
		KoboldsStructures.REGISTRY.register(bus);
	}

	private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
			workQueue.add(new Tuple<>(action, tick));
		}
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setB(work.getB() - 1);
			if (work.getB() == 0) {
				actions.add(work);
			}
		});
		actions.forEach(e -> e.getA().run());
		workQueue.removeAll(actions);
	}
}