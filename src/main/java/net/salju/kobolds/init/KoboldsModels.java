package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.model.*;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.ModelLayerLocation;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KoboldsModels {
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
		event.registerLayerDefinition(ZOMBOLD, ZomboldModel::createBodyLayer);
		event.registerLayerDefinition(SKELEBOLD, SkeleboldModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_SKULL, KoboldSkullModel::createBodyLayer);
	}
}