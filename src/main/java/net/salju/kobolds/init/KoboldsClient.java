package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.model.*;
import net.salju.kobolds.client.renderer.*;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.resources.Identifier;

@EventBusSubscriber(value = Dist.CLIENT)
public class KoboldsClient {
    public static final ArmorModelSet<ModelLayerLocation> KOBOLD_ARMOR = registerArmorSet("kobold_armor");
	public static final ModelLayerLocation KOBOLD = registerModel("kobold", "main");
	public static final ModelLayerLocation ZOMBOLD = registerModel("zombold", "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(KOBOLD_ARMOR.head(), KoboldArmorModel::createHeadLayer);
        event.registerLayerDefinition(KOBOLD_ARMOR.chest(), KoboldArmorModel::createBodyLayer);
        event.registerLayerDefinition(KOBOLD_ARMOR.legs(), KoboldArmorModel::createLegsLayer);
        event.registerLayerDefinition(KOBOLD_ARMOR.feet(), KoboldArmorModel::createBootsLayer);
		event.registerLayerDefinition(KOBOLD, KoboldModel::createBodyLayer);
		event.registerLayerDefinition(ZOMBOLD, KoboldModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(KoboldsMobs.KOBOLD.get(), KoboldRenderer::new);
		event.registerEntityRenderer(KoboldsMobs.KOBOLD_ZOMBIE.get(), KoboldZombieRenderer::new);
	}

    private static ArmorModelSet<ModelLayerLocation> registerArmorSet(String path) {
        return new ArmorModelSet(registerModel(path, "helmet"), registerModel(path, "chestplate"), registerModel(path, "leggings"), registerModel(path, "boots"));
    }

    private static ModelLayerLocation registerModel(String path, String model) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Kobolds.MODID, path), model);
    }
}