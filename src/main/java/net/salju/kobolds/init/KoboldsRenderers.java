package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.renderer.*;
import net.salju.kobolds.client.model.KoboldSkullModel;
import net.salju.kobolds.block.KoboldSkull;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KoboldsRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(KoboldsMobs.KOBOLD.get(), KoboldRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_WARRIOR.get(), KoboldWarriorRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_ENCHANTER.get(), KoboldEnchanterRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_ENGINEER.get(), KoboldEngineerRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_PIRATE.get(), KoboldPirateRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_CAPTAIN.get(), KoboldCaptainRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_CHILD.get(), KoboldChildRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_ZOMBIE.get(), KoboldZombieRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_SKELETON.get(), KoboldSkeletonRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_RASCAL.get(), KoboldRascalRenderer::new);
	}

	@SubscribeEvent
	public static void registerSkullRenderers(EntityRenderersEvent.CreateSkullModels event) {
		event.registerSkullModel(KoboldSkull.Types.SKELEBOLD, new KoboldSkullModel(event.getEntityModelSet().bakeLayer(KoboldsModels.KOBOLD_SKULL)));
	}

	@SubscribeEvent
	public static void clientSetupEvent(FMLClientSetupEvent event) {
		event.enqueueWork(() -> SkullBlockRenderer.SKIN_BY_TYPE.put(KoboldSkull.Types.SKELEBOLD, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton.png")));
	}
}