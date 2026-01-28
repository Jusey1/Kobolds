package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.block.*;
import net.salju.kobolds.client.model.*;
import net.salju.kobolds.client.renderer.*;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.resources.Identifier;

@EventBusSubscriber(value = Dist.CLIENT)
public class KoboldsClient {
    public static final ArmorModelSet<ModelLayerLocation> KOBOLD_ARMOR = registerArmorSet("kobold_armor");
	public static final ModelLayerLocation KOBOLD = registerModel("kobold", "main");
	public static final ModelLayerLocation KOBOLD_RASCAL = registerModel("kobold_rascal", "main");
	public static final ModelLayerLocation KOBOLD_CHILD = registerModel("kobold_child", "main");
	public static final ModelLayerLocation KOBOLD_SKULL = registerModel("kobold_skull", "main");
	public static final ModelLayerLocation KOBOLD_WITHER_SKULL = registerModel("kobold_wither_skull", "main");
	public static final ModelLayerLocation SKELEBOLD = registerModel("skelebold", "main");
	public static final ModelLayerLocation WITHERBOLD = registerModel("witherbold", "main");
	public static final ModelLayerLocation ZOMBOLD = registerModel("zombold", "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(KOBOLD_ARMOR.head(), KoboldArmorModel::createHeadLayer);
        event.registerLayerDefinition(KOBOLD_ARMOR.chest(), KoboldArmorModel::createBodyLayer);
        event.registerLayerDefinition(KOBOLD_ARMOR.legs(), KoboldArmorModel::createLegsLayer);
        event.registerLayerDefinition(KOBOLD_ARMOR.feet(), KoboldArmorModel::createBootsLayer);
		event.registerLayerDefinition(KOBOLD, KoboldModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_RASCAL, RascalModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_CHILD, KoboldChildModel::createBodyLayer);
		event.registerLayerDefinition(ZOMBOLD, KoboldModel::createBodyLayer);
		event.registerLayerDefinition(SKELEBOLD, SkeleboldModel::createBodyLayer);
		event.registerLayerDefinition(WITHERBOLD, SkeleboldModel::createBodyLayer);
		event.registerLayerDefinition(KOBOLD_WITHER_SKULL, KoboldSkullModel::createBodyLayer);
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
		event.registerEntityRenderer(KoboldsMobs.WITHERBOLD.get(), KoboldWitherRenderer::new);
	}

	@SubscribeEvent
	public static void registerSkullRenderers(EntityRenderersEvent.CreateSkullModels event) {
		event.registerSkullModel(KoboldSkull.Types.SKELEBOLD, KOBOLD_SKULL, Identifier.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton.png"));
		event.registerSkullModel(KoboldWitherSkull.Types.WITHERBOLD, KOBOLD_WITHER_SKULL, Identifier.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton_wither.png"));
	}

	@SubscribeEvent
	public static void clientSetupEvent(FMLClientSetupEvent event) {
		event.enqueueWork(() -> SkullBlockRenderer.SKIN_BY_TYPE.put(KoboldSkull.Types.SKELEBOLD, Identifier.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton.png")));
		event.enqueueWork(() -> SkullBlockRenderer.SKIN_BY_TYPE.put(KoboldWitherSkull.Types.WITHERBOLD, Identifier.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton_wither.png")));
	}

    private static ArmorModelSet<ModelLayerLocation> registerArmorSet(String path) {
        return new ArmorModelSet(registerModel(path, "helmet"), registerModel(path, "chestplate"), registerModel(path, "leggings"), registerModel(path, "boots"));
    }

    private static ModelLayerLocation registerModel(String path, String model) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kobolds.MODID, path), model);
    }
}