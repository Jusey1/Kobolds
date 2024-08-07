package net.salju.kobolds.init;

import net.salju.kobolds.client.model.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class KoboldsModels {
	public static final ModelLayerLocation KOBOLD = new ModelLayerLocation(new ResourceLocation("kobolds", "kobold"), "main");
	public static final ModelLayerLocation KOBOLD_RASCAL = new ModelLayerLocation(new ResourceLocation("kobolds", "kobold_rascal"), "main");
	public static final ModelLayerLocation KOBOLD_CHILD = new ModelLayerLocation(new ResourceLocation("kobolds", "kobold_child"), "main");
	public static final ModelLayerLocation KOBOLD_ARMOR_INNER = new ModelLayerLocation(new ResourceLocation("kobolds", "kobold_armor_inner"), "main");
	public static final ModelLayerLocation KOBOLD_ARMOR_OUTER = new ModelLayerLocation(new ResourceLocation("kobolds", "kobold_armor_outer"), "main");
	public static final ModelLayerLocation KOBOLD_SKULL = new ModelLayerLocation(new ResourceLocation("kobolds", "kobold_skull"), "main");
	public static final ModelLayerLocation SKELEBOLD = new ModelLayerLocation(new ResourceLocation("kobolds", "skelebold"), "main");
	public static final ModelLayerLocation ZOMBOLD = new ModelLayerLocation(new ResourceLocation("kobolds", "zombold"), "main");

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