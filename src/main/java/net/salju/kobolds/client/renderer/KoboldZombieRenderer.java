package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.entity.KoboldZombie;
import net.salju.kobolds.client.renderer.layers.KoboldZombieEyesLayer;
import net.salju.kobolds.client.model.ZomboldModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldZombieRenderer extends MobRenderer<KoboldZombie, AbstractKoboldState, ZomboldModel<AbstractKoboldState>> {
	public KoboldZombieRenderer(EntityRendererProvider.Context context) {
		super(context, new ZomboldModel(context.bakeLayer(KoboldsClient.ZOMBOLD)), 0.36f);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemRenderer()));
		this.addLayer(new KoboldZombieEyesLayer<>(this));
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_INNER)), new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_OUTER)), context.getEquipmentRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractKoboldState zombo) {
		return zombo.texture;
	}

	@Override
	public AbstractKoboldState createRenderState() {
		return new AbstractKoboldState();
	}

	@Override
	public void extractRenderState(KoboldZombie zombo, AbstractKoboldState state, float f1) {
		super.extractRenderState(zombo, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(zombo, state, f1);
		if (zombo.getName().getString().equals("James") && zombo.getZomboType().equals("enchanter")) {
			state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/zombie_james.png");
		} else {
			state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/zombie_" + zombo.getZomboType() + ".png");
		}
		state.isAggressive = zombo.isAggressive();
		state.isLeftHanded = zombo.isLeftHanded();
		state.getZomboType = zombo.getZomboType();
		state.isZomboConverting = zombo.isConvert();
	}

	@Override
	protected boolean isShaking(AbstractKoboldState zombo) {
		return (zombo.isZomboConverting);
	}

	@Override
	public void render(AbstractKoboldState zombo, PoseStack stack, MultiBufferSource buffer, int i) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(zombo, stack, buffer, i);
		stack.popPose();
	}
}