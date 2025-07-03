package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.block.KoboldSkull;
import net.salju.kobolds.client.model.*;
import net.salju.kobolds.client.renderer.*;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;

@EventBusSubscriber(value = Dist.CLIENT)
public class KoboldsClient {
	public static final ModelLayerLocation KOBOLD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold"), "main");
	public static final ModelLayerLocation KOBOLD_RASCAL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold_rascal"), "main");
	public static final ModelLayerLocation KOBOLD_CHILD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold_child"), "main");
	public static final ModelLayerLocation KOBOLD_ARMOR_INNER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold_armor_inner"), "main");
	public static final ModelLayerLocation KOBOLD_ARMOR_OUTER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold_armor_outer"), "main");
	public static final ModelLayerLocation KOBOLD_SKULL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold_skull"), "main");
	public static final ModelLayerLocation SKELEBOLD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "skelebold"), "main");
	public static final ModelLayerLocation ZOMBOLD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "zombold"), "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(KOBOLD_ARMOR_INNER, KoboldArmorModel::createInnerArmorLayer);
		event.registerLayerDefinition(KOBOLD_ARMOR_OUTER, KoboldArmorModel::createOuterArmorLayer);
		event.registerLayerDefinition(KOBOLD, KoboldModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_RASCAL, RascalModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_CHILD, KoboldChildModel::createBodyLayer);
		event.registerLayerDefinition(ZOMBOLD, KoboldModel::createBodyLayer);
		event.registerLayerDefinition(SKELEBOLD, SkeleboldModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_SKULL, KoboldSkullModel::createBodyLayer);
	}

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
		event.registerSkullModel(KoboldSkull.Types.SKELEBOLD, KOBOLD_SKULL);
	}

	@SubscribeEvent
	public static void clientSetupEvent(FMLClientSetupEvent event) {
		event.enqueueWork(() -> SkullBlockRenderer.SKIN_BY_TYPE.put(KoboldSkull.Types.SKELEBOLD, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton.png")));
	}
}