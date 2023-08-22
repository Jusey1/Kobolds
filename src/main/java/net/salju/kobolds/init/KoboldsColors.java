package net.salju.kobolds.init;

import net.salju.kobolds.item.KoboldPotionUtils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KoboldsColors {
	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, layer) -> {
			return layer > 0 ? -1 : KoboldPotionUtils.getColor(stack);
		}, KoboldsItems.KOBOLD_POTION.get(), KoboldsItems.KOBOLD_POTION_INFINITY.get());
	}
}